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

        if echo "$decac_moins_b" | grep -i -e "erreur" -e "error"; then
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


for cas_de_test in src/test/deca/context/valid/*.deca
do
    echo "----- Test de decac -p pour le fichier $cas_de_test :  -----"
    echo $cas_de_test | grep -q "overflow"
    if [ "$?" -ne 1 ]; then
        echo "Test d'overflow, non pris en charge"
        echo "----- OK -----"
        echo ""
    else
        decac_moins_b=$(decac -p "$cas_de_test")

        if [ "$?" -ne 0 ]; then
            echo "ERREUR: decac -p a termine avec un status different de zero."
            exit 1
        fi

        if [ "$decac_moins_b" = "" ]; then
            echo "ERREUR: decac -p n'a produit aucune sortie"
            exit 1
        fi

        if echo "$decac_moins_b" | grep -i -e "erreur" -e "error"; then
            echo "ERREUR: La sortie de decac -p contient erreur ou error"
            exit 1
        fi
    fi

    echo "----- OK -----"
    echo ""
done

echo ""
echo "############# TEST DE L'OPTION DU COMPILATEUR -r X #############"
echo ""

rmax=16

for i in `seq 1 20`
do
    if [ "$i" -le 16 ] && [ "$i" -gt 2 ];
    then
        for cas_de_test in src/test/deca/codegen/valid/*.deca
        do
            echo "----- Test de decac -r $1 pour le fichier $cas_de_test :  -----"
            decac_moins_r=$(decac -r "$i" "$cas_de_test")

            if [ "$?" -ne 0 ]; then
                echo "ERREUR: decac -r "$i" a termine avec un status different de zero"
                exit 1
            fi

            if [ "$decac_moins_r" = "" ]; then
                echo "ERREUR: decac -r "$i" n'a produit aucune sortie"
                exit 1
            fi

            if echo "$decac_moins_r" | grep -i -e "erreur" -e "error"; then
                echo "ERREUR: La sortie de decac -r "$i" contient erreur ou error"
                exit 1
            fi

            echo "----- OK -----"
            echo ""
        done

    else
        cas_de_test="src/test/deca/context/valid/plus_plus_minus.deca"
        echo "----- Test de decac -r $i pour le fichier $cas_de_test :  -----"
        decac_moins_r=$(decac -r "$i" "$cas_de_test")

        if [ "$?" -ne 1 ]; then
            echo "ERREUR: decac -r "$i" a termine avec un status égal à zero : $? imprévu."
            exit 1
        else
            echo "----- KO -----"
            echo ""
        fi
    fi
done