Pour exécuter notre projet, il faut suivre les étapes suivantes :
1-Créer le Lexer avec jflex à partir de analyseurLexical.flex .
2-Exécuter la classe Main, avec deux arguments. Le premier est le nom du fichier simpletext à analyser, le second le nom du fichier html à créer.

Nous avons traité toutes les étapes du projet jusqu'aux abréviations (troisième étape, section 6). Nous n'avons pas rencontré d'erreurs, et nous avons fourni quelques fichiers sur lesquels tester le projet.

Quelques remarques sont à faire concernant le code :
1-Nous avons légèrement modifié la grammaire. Nous avons remplacé la règle DECLARATIONS → \set{ ID }{ CONSTANTE_COULEUR } DECLARATIONS par DECLARATIONS → DECLARATION DECLARATIONS . Nous avons aussi ajouté la règle DECLARATION → \set{ ID }{ CONSTANTE_COULEUR } (même chose pour les abréviations) . Cela est plus cohérent, par rapport aux suite d'élements ou d'items.
2-Selon la situation, le terminal CONSTANTE_COULEUR doit soit être interprété comme le code d'une couleur dans une déclaration, soit comme un mot. Nous avons géré cela dans le Parser et le flex.
