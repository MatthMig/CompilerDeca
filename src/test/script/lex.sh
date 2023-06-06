#! /bin/sh

# Auteur : gl03
# Version initiale : 21/04/2023
# Mise à jour : 05/06/2023

echo "Lancement du script "$0" ......"

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"

test_lex_valid () {
    echo $1
    if test_lex $1 2>&1 | \
        grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec inattendu pour test_lex pour le fichier $1"
        exit 1
    else
        echo "Succes attendu de test_lex pour le fichier $1"
    fi
}

test_lex_invalid () {
    echo $1
    if test_lex $1 2>&1 | \
        grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec attendu pour test_lex sur $1"
    else
        echo "Succes inattendu de test_lex sur $1"
        exit 1
    fi
}

for cas_de_test in src/test/deca/lex/valid/*.deca
do
    echo "----- Test de lexer pour le fichier $cas_de_test (valide) :  -----"
    test_lex_valid "$cas_de_test" || exit 1
    echo "----- OK -----"
    echo ""
done

for cas_de_test in src/test/deca/lex/invalid/*.deca
do
    echo "----- Test de lexer pour le fichier $cas_de_test (invalide) :  -----"
    test_lex_invalid "$cas_de_test" || exit 1
    echo "----- KO -----"
    echo ""
done