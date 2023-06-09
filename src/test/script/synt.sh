#! /bin/sh

# Auteur : gl03
# Version initiale : 21/04/2023
# Mise à jour : 05/06/2023

echo "Lancement du script "$0" ......"

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"

test_synt_valid () {
    echo $1
    if test_synt $1 2>&1 | \
        grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec inattendu pour test_synt pour le fichier $1"
        return 1
    else
        echo "Succes attendu de test_synt pour le fichier $1"
    fi
}

test_synt_invalid () {
    echo $1
    if test_synt $1 2>&1 | \
        grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec attendu pour test_synt sur $1"
    else
        echo "Succes inattendu de test_synt sur $1"
        return 1
    fi
}

i=0
success=0
for cas_de_test in src/test/deca/syntax/valid/*.deca
do
    echo "----- Test de syntaxe pour le fichier $cas_de_test (valide) :  -----"
    if test_synt_valid "$cas_de_test"
    then
        success=$((success+1))
        echo "----- OK -----"
    else
        echo "----- KO -----"
    fi
    echo ""

    i=$((i+1))
done

for cas_de_test in src/test/deca/syntax/invalid/*.deca
do
    echo "----- Test de syntaxe pour le fichier $cas_de_test (invalide) :  -----"
    if test_synt_invalid "$cas_de_test"
    then
        success=$((success+1))
        echo "----- OK -----"
    else
        echo "----- KO -----"
    fi
    echo ""

    i=$((i+1))
done


echo "### SCORE: ${success} PASSED / ${i} TESTS ###"

if [ "$i" -gt "$success" ]
then
    exit 1
fi
