cat << "EOF"

 __                            __        __                     
/  |                          /  |      /  |                    
$$ |   __  __    __   ______  $$/   ____$$ |  ______    ______  
$$ |  /  |/  |  /  | /      \ /  | /    $$ | /      \  /      \ 
$$ |_/$$/ $$ |  $$ | $$$$$$  |$$ |/$$$$$$$ | $$$$$$  |/$$$$$$  |
$$   $$<  $$ |  $$ | /    $$ |$$ |$$ |  $$ | /    $$ |$$ |  $$ |
$$$$$$  \ $$ \__$$ |/$$$$$$$ |$$ |$$ \__$$ |/$$$$$$$ |$$ \__$$ |
$$ | $$  |$$    $$/ $$    $$ |$$ |$$    $$ |$$    $$ |$$    $$/ 
$$/   $$/  $$$$$$/   $$$$$$$/ $$/  $$$$$$$/  $$$$$$$/  $$$$$$/  
                                                                
                                                                
                                                                

EOF

CI=true
yarn
yarn build
yarn test:nowatch
cd ..
mvn clean install
mvn clean test