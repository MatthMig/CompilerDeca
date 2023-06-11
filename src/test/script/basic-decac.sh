#! /bin/sh

# Test de l'interface en ligne de commande de decac.
# On ne met ici qu'un test trivial, a vous d'en ecrire de meilleurs.

echo "Lancement du script "$0" ......"

 PATH=./src/main/bin:"$PATH"

echo ""
echo "############# TEST DE L'OPTION DU COMPILATEUR -v #############"
echo ""

for cas_de_test in src/test/deca/context/valid/*.deca
do
    echo "----- Test de decac -v pour le fichier $cas_de_test :  -----"
    echo $cas_de_test | grep -q "overflow"
    if [ "$?" -ne 1 ]; then
        echo "Test d'overflow, non pris en charge"
        echo "----- OK -----"
        echo ""
    else
        decac_moins_v=$(decac -v "$cas_de_test")

        if [ "$?" -ne 0 ]; then
            echo "ERREUR: decac -v a termine avec un status different de zero."
            exit 1
        fi

        if echo "$decac_moins_v" | grep -i -e "erreur" -e "error"; then
            echo "ERREUR: La sortie de decac -v contient erreur ou error"
            exit 1
        fi

        echo "----- OK -----"
        echo ""
    fi
done

echo ""
echo "############# TEST DE L'OPTION DU COMPILATEUR -p #############"
echo ""


i=0
success=0
for cas_de_test in src/test/deca/context/valid/*.deca
do
    echo "----- Test de decac -p pour le fichier $cas_de_test :  -----"
    echo $cas_de_test | grep -q "overflow"
    if [ "$?" -ne 1 ]; then
        echo "Test d'overflow, non pris en charge"
        echo "----- OK -----"
        i=$((i-1))
        echo ""
    else
        decac -p "$cas_de_test" > tmp
        decac_moins_p=$(cat tmp)

        if [ "$?" -ne 0 ]; then
            echo "ERREUR: decac -p a termine avec un status different de zero."
            echo "----- KO -----"

        elif [ "$decac_moins_p" = "" ]; then
            echo "ERREUR: decac -p n'a produit aucune sortie"
            echo "----- KO -----"

        elif echo "$decac_moins_p" | grep -i -e "erreur" -e "error"; then
            echo "ERREUR: La sortie de decac -p contient erreur ou error"
            echo "----- KO -----"

            synt=$(test_synt $cas_de_test |  sed -e 's/\[[0-9]*, [0-9]*\]/\[a,a\]/g' )
            synt_b=$(test_synt tmp |  sed -e 's/\[[0-9]*, [0-9]*\]/\[a,a\]/g' )

        elif [ "$synt" != "$synt_b" ]; then
            echo "ERREUR: La sortie de decac -p n'a pas le même arbre que le fichier $cas_de_test"
            echo "----- KO -----"

        else
            success=$((success+1))
            echo "----- OK -----"
        fi
    fi
    echo ""
    i=$((i+1))
done

echo "### SCORE: ${success} PASSED / ${i} TESTS ###"

if [ "$i" -gt "$success" ]
then
    exit 1
fi

i=0
success=0

rm tmp

echo ""
echo "############# TEST DES OPTIONS DU COMPILATEUR -P -r X #############"
echo ""

tests=0
tests_success=0
ima=0
ima_success=0
bad=0
bad_success=0

for i in `seq 1 20`
do
    if [ "$i" -le 16 ] && [ "$i" -gt 2 ];
    then
        echo "----- Test de decac -P -r $i pour les fichiers src/test/deca/codegen/valid/*.deca :  -----"
        decac_moins_P_r=$(decac -P -r "$i" $(ls src/test/deca/codegen/valid/*.deca))

        if [ $? -eq 0 ]; then
            tests_success=$((tests_success+1))

            echo "\t----- Compilation parallèle et limitée réussie, vérification des .ass  -----"
            for cas_de_test in src/test/deca/codegen/valid/*.deca
            do
                assfile=$(echo $cas_de_test | sed -e 's/\.deca/\.ass/g')
                resultat=$(ima $assfile)
                attendufile=$(echo $cas_de_test | sed -e 's/\.deca/\.expected/g')
                attendu=$(cat $attendufile)

                if [ "$resultat" = "$attendu" ]; then
                    ima_success=$((ima_success+1))
                else
                    echo "Résultat inattendu de ima pour le fichier $cas_de_test"
                    echo "Résultat obtenu :"
                    echo $resultat
                    echo "Résultat attendu :"
                    echo $attendu
                fi
                ima=$((ima+1))
            done
            echo "\t----- .ass vérifiés -----"
            echo "----- OK -----"
        else
            echo "----- KO -----"
        fi
        tests=$((tests+1))
        echo
    else
        cas_de_test="src/test/deca/codegen/valid/plus_plus_minus.deca"
        echo "----- Test de decac -r $i (doit être un échec) :  -----"
        decac_moins_r=$(decac -r "$i" "$cas_de_test")

        if [ "$?" -ne 1 ]; then
            echo "ERREUR: decac -r "$i" a termine avec un status égal à zero : $? imprévu."
            echo "----- KO -----"
        else
            bad_success=$((bad_success+1))
            echo "----- OK -----"
        fi
        echo ""
        bad=$((bad+1))
    fi
done

echo "Compilation parallèle avec limitation des registres :"
echo "### SCORE: ${tests} PASSED / ${tests_success} TESTS ###"
echo

echo "Programme à nombre de registres restreints exécutés :"
echo "### SCORE: ${ima} PASSED / ${ima_success} TESTS ###"
echo

echo "Tests de vérification d'échec en cas de mauvaise valeur pour la quantité de registres :"
echo "### SCORE: ${bad} PASSED / ${bad_success} TESTS ###"


if [ "$tests" -gt "$tests_success" ] | [ "$ima" -gt "$ima_success" ] | [ "$bad" -gt "$bad_success" ]
then
    exit 1
fi
