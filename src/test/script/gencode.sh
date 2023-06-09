#! /bin/sh

# Auteur : gl03
# Version initiale : 21/04/2023
# Mise à jour : 05/06/2023

echo "Lancement du script "$0" ......"

cd "$(dirname "$0")"/../../.. || return 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"

test_codegen () {
    # $1 = premier argument.

    # Compilation du deca
    if decac $1
    then
        echo "Compilation de $1 réalisée."

        # Est-ce qu'il y a un .ass ?
        if [ ! -f $2 ]; then
            echo "Fichier $2 non généré."
            return 1
        fi

        # Est-ce qu'on peut utiliser ce code ima ?
        resultat=$(ima "$2") || ( echo "Erreur lors de l'éxecution du programme ima" ; return 1 )
        attendu=$(cat "$3")

        # Produit-il le résultat attendu ?
        if [ ! "$resultat" = "$attendu" ]; then
            echo "Résultat inattendu de ima:"
            echo "$resultat"
            return 1
        fi

    else
        echo "Compilation impossible de $1."
        return 1
    fi
}

i=0
success=0
for cas_de_test in src/test/deca/codegen/valid/*.deca
do
    rm -f ./src/test/deca/codegen/valid/provided/$(echo "$cas_de_test" | cut -d "." -f1).ass 2>/dev/null
    echo "----- Test de génération de code pour le fichier $cas_de_test :  -----"
    if test_codegen "$cas_de_test" "$(echo $cas_de_test | cut -d "." -f1).ass" "$(echo $cas_de_test | cut -d "." -f1).expected"
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
