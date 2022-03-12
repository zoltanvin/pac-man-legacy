import game.pm.PMAction;
import game.pm.strategies.Strategy;
import game.pm.PMGame;
public class Agent extends Strategy {
        int previousDirection = 6;
        int previousSecondDirection = 6;
        boolean visszafeleMegyekE(int fd) { // ha az elozo vagy az azelotti irany uygyanaz mint a mostani akkor visszafele megy
                if (fd == 0 && (previousDirection == 2 || previousSecondDirection == 2)) {
                        return true;
                }
                if (fd == 1 && (previousDirection == 3 || previousSecondDirection == 3)) {
                        return true;
                }
                if (fd == 2 && (previousDirection == 0 || previousSecondDirection == 0)) {
                        return true;
                }
                if (fd == 3 && (previousDirection == 1 || previousSecondDirection == 1)) {
                        return true;
                }
                return false;
        }
        int tavolsagpacmankajafuggveny(final int id, final PMGame game, int kajai, int kajaj) { //legkozelebbi pont a pacmantol
                int tavolsagipacmankaja = game.pacmans[id].getTileI() - kajai;
                int tavolsagjpacmankaja = game.pacmans[id].getTileJ() - kajaj;
                if (tavolsagipacmankaja < 0) {
                        tavolsagipacmankaja = -1 * tavolsagipacmankaja;
                } //abszolut érték
                if (tavolsagjpacmankaja < 0) {
                        tavolsagjpacmankaja = -1 * tavolsagjpacmankaja;
                } //abszolút érték
                int pacmankajatavolsaglegvonalban = tavolsagipacmankaja + tavolsagjpacmankaja;
                return pacmankajatavolsaglegvonalban;
        }
        int legkozelebbiSzellem(final int id, final PMGame game) { //ugyanaz csak szellemre
                int vegsoSzellem = 5;
                int minimumtavolsag = 500;
                for (int i = 0; i < 4; i++) {
                        int tavlosagszellempacmani = game.ghosts[i].getTileI() - game.pacmans[id].getTileI();
                        int tavlosagszellempacmanj = game.ghosts[i].getTileJ() - game.pacmans[id].getTileJ();
                        if (tavlosagszellempacmani < 0) {
                                tavlosagszellempacmani = -1 * tavlosagszellempacmani;
                        } //abszolut érték
                        if (tavlosagszellempacmanj < 0) {
                                tavlosagszellempacmanj = -1 * tavlosagszellempacmanj;
                        } //abszolut érték
                        int vegsoszellemTavolsag = tavlosagszellempacmani + tavlosagszellempacmanj;
                        if (vegsoszellemTavolsag < minimumtavolsag && game.ghosts[i].getTileI() != 14) { //ez a kezdopont azert nem
                                minimumtavolsag = vegsoszellemTavolsag;
                                vegsoSzellem = i;
                        }
                }
                return vegsoSzellem;
        }
        int s[][] = new int[30][27];
        int menekuljTole = 5;
        int vegsoSzellem = 5;
        int szellemSzamaKovetni(final int id, final PMGame game) { //megnezi melyik szellem van a legkozelebb
                int minimumtavolsag = 500;
                menekuljTole = 5;
                vegsoSzellem = 5;
                for (int i = 0; i < 4; i++) {
                        int tavlosagszellempacmani = game.ghosts[i].getTileI() - game.pacmans[id].getTileI();
                        int tavlosagszellempacmanj = game.ghosts[i].getTileJ() - game.pacmans[id].getTileJ();
                        if (tavlosagszellempacmani < 0) { //abszolut érték
                                tavlosagszellempacmani = -1 * tavlosagszellempacmani;
                        }
                        if (tavlosagszellempacmanj < 0) { //abszolut érték
                                tavlosagszellempacmanj = -1 * tavlosagszellempacmanj;
                        }
                        int vegsoszellemTavolsag = tavlosagszellempacmani + tavlosagszellempacmanj;
                        if (vegsoszellemTavolsag < minimumtavolsag && game.ghosts[i].getTileI() != 14) { // kezdo allapot nem
                                minimumtavolsag = vegsoszellemTavolsag;
                                if (minimumtavolsag < 4) { //ha 4nel kissebb a tav akkor ez a szellem lesz majd amit kovetni kell ha megeheto
                                        vegsoSzellem = i;
                                }
                                if (minimumtavolsag < 4) { //ha 4nel kissebb a tav akkor ettol menekulni kell
                                        menekuljTole = i;
                                }
                        }
                }
                return vegsoSzellem;
        }
        int kovetes(final int id, final PMGame game, int ths) { //ha eheto akkor kovesse ezekben a koordinatakban
                int finaldirection = previousDirection;
                if (ths == 5) {
                        finaldirection = 5;
                } else
                if (game.ghosts[ths].tillFrightened > 0) {
                        if (game.ghosts[ths].getTileI() > game.pacmans[id].getTileI()) {
                                finaldirection = 2;
                        } else
                        if (game.ghosts[ths].getTileJ() < game.pacmans[id].getTileJ()) {
                                finaldirection = 1;
                        } else
                        if (game.ghosts[ths].getTileJ() > game.pacmans[id].getTileJ()) {
                                finaldirection = 3;
                        } else
                        if (game.ghosts[ths].getTileI() < game.pacmans[id].getTileI()) {
                                finaldirection = 0;
                        }
                }
                return finaldirection;
        }
        @Override
        public int getDirection(final int id, final PMGame game) {
                int value[][] = new int[30][27];
                int minimumtavolsagpacmantol = 50;
                int legjobbkajaI = 0;
                int legjobbkajaJ = 0;
                for (int i = 30; i >= 0; i--) {
                        for (int j = 27; j >= 0; j--) {
                                if (game.maze[i][j] == 2 || game.maze[i][j] == 4) {
                                        value[i][j] = game.maze[i][j];
                                        if (tavolsagpacmankajafuggveny(id, game, i, j) < minimumtavolsagpacmantol) { //megadja a legjobb jaja helyet
                                                minimumtavolsagpacmantol = tavolsagpacmankajafuggveny(id, game, i, j);
                                                legjobbkajaI = i;
                                                legjobbkajaJ = j;
                                        }
                                }
                        }
                }
                int finaldirection = 5;
                boolean isWallAtRight; // ez a negy megnezi hogy a pacpantol negy iranyba van e fal
                if (game.pacmans[id].getTileJ() == 27) { //azert mert kulonben exceptiont dob
                        isWallAtRight = false;
                } else {
                        isWallAtRight = game.maze[game.pacmans[id].getTileI()][game.pacmans[id].getTileJ() + 1] == 1;
                }
                boolean isWallAtLeft;
                if (game.pacmans[id].getTileJ() == 0) { //azert mert kulonben exceptiont dob
                        isWallAtLeft = false;
                } else {
                        isWallAtLeft = game.maze[game.pacmans[id].getTileI()][game.pacmans[id].getTileJ() - 1] == 1;
                }
                boolean isWallAtTop = game.maze[game.pacmans[id].getTileI() - 1][game.pacmans[id].getTileJ()] == 1;
                boolean isWallAtBottom = game.maze[game.pacmans[id].getTileI() + 1][game.pacmans[id].getTileJ()] == 1;
                if (legjobbkajaI > game.pacmans[id].getTileI()) { //ezek ha abba az iranyba van a kaja arra induljon majd
                        finaldirection = 2;
                } else
                if (legjobbkajaJ < game.pacmans[id].getTileJ()) {
                        finaldirection = 1;
                } else
                if (legjobbkajaJ > game.pacmans[id].getTileJ()) {
                        finaldirection = 3;
                } else
                if (legjobbkajaI < game.pacmans[id].getTileI()) {
                        finaldirection = 0;
                }
                boolean iswalled = (finaldirection == 0 && isWallAtTop) || //hogyha egy darab falba utkozott akkor kanyarodjon a kaja iranyaba
                        (finaldirection == 1 && isWallAtLeft) ||
                        (finaldirection == 2 && isWallAtBottom) ||
                        (finaldirection == 3 && isWallAtRight);
                boolean visszae = visszafeleMegyekE(finaldirection);
                if (legjobbkajaI > game.pacmans[id].getTileI()) {
                        finaldirection = 2;
                } else
                if (legjobbkajaJ < game.pacmans[id].getTileJ()) {
                        finaldirection = 1;
                } else
                if (legjobbkajaJ > game.pacmans[id].getTileJ()) {
                        finaldirection = 3;
                } else
                if (legjobbkajaI < game.pacmans[id].getTileI()) {
                        finaldirection = 0;
                }
                if (iswalled || visszae) { //hogyha ket fal van azaz sarok, akkor arra menjen amerrol nem jott
                        if (isWallAtRight && isWallAtTop) {
                                if (3 == previousDirection || 3 == previousSecondDirection) {
                                        finaldirection = 2;
                                } else
                                if (0 == previousDirection || 0 == previousSecondDirection) {
                                        finaldirection = 1;
                                }
                        } else
                        if (isWallAtRight && isWallAtBottom) {
                                if (2 == previousDirection || 2 == previousSecondDirection) {
                                        finaldirection = 1;
                                } else
                                if (3 == previousDirection || 3 == previousSecondDirection) {
                                        finaldirection = 0;
                                }
                        } else
                        if (isWallAtLeft && isWallAtTop) {
                                if (1 == previousDirection || 1 == previousSecondDirection) {
                                        finaldirection = 2;
                                } else
                                if (0 == previousDirection || 0 == previousSecondDirection) {
                                        finaldirection = 3;
                                }
                        } else
                        if (isWallAtLeft && isWallAtBottom) {
                                if (1 == previousDirection || 1 == previousSecondDirection) {
                                        finaldirection = 0;
                                } else
                                if (2 == previousDirection || 2 == previousSecondDirection) {
                                        finaldirection = 3;
                                }
                        } else //hogyha fal van elotte es arra akarna menni, akkor forduljon masmerre
                                if (isWallAtRight && (3 == previousDirection || 3 == previousSecondDirection)) {
                                        finaldirection = 0;
                                } else
                        if (isWallAtTop && (0 == previousDirection || 0 == previousSecondDirection)) {
                                finaldirection = 1;
                        } else
                        if (isWallAtLeft && (1 == previousDirection || 1 == previousSecondDirection)) {
                                finaldirection = 0;
                        } else
                        if (isWallAtBottom && (2 == previousDirection || 2 == previousSecondDirection)) {
                                finaldirection = 1;
                        }
                        if (visszae) { //ez akkor kell ha negyes keresztezodes van es beakadt
                                if (previousDirection == 1 || previousDirection == 3) {
                                        if (legjobbkajaI < game.pacmans[id].getTileI()) {
                                                finaldirection = 0;
                                        }
                                        if (legjobbkajaI > game.pacmans[id].getTileI()) {
                                                finaldirection = 2;
                                        }
                                }
                                if (previousDirection == 0 || previousDirection == 2) {
                                        if (legjobbkajaJ < game.pacmans[id].getTileJ()) {
                                                finaldirection = 3;
                                        }
                                        if (legjobbkajaJ < game.pacmans[id].getTileJ()) {
                                                finaldirection = 1;
                                        }
                                }
                        }
                }
                szellemSzamaKovetni(id, game); //kell a benne levo adat
                if (menekuljTole < 4 && game.ghosts[menekuljTole].tillFrightened == 0) { //ez az ha nem lehet megenni a szellemet
                        if (game.ghosts[menekuljTole].getTileI() >= game.pacmans[id].getTileI()) { //falba utkozesek lekezelese
                                if (game.ghosts[menekuljTole].getTileJ() > game.pacmans[id].getTileJ()) {
                                        if (isWallAtLeft) {
                                                finaldirection = 0;
                                        }
                                        if (isWallAtTop) {
                                                finaldirection = 1;
                                        }
                                        if (isWallAtLeft == false && isWallAtTop == false) {
                                                finaldirection = 0;
                                        }
                                }
                        }
                        if (game.ghosts[menekuljTole].getTileI() >= game.pacmans[id].getTileI()) {
                                if (game.ghosts[menekuljTole].getTileJ() < game.pacmans[id].getTileJ()) {
                                        if (isWallAtRight) {
                                                finaldirection = 0;
                                        }
                                        if (isWallAtTop) {
                                                finaldirection = 3;
                                        }
                                        if (isWallAtRight == false && isWallAtTop == false) {
                                                finaldirection = 3;
                                        }
                                }
                        }
                        if (game.ghosts[menekuljTole].getTileI() < game.pacmans[id].getTileI()) {
                                if (game.ghosts[menekuljTole].getTileJ() <= game.pacmans[id].getTileJ()) {
                                        if (isWallAtRight) {
                                                finaldirection = 2;
                                        }
                                        if (isWallAtBottom) {
                                                finaldirection = 3;
                                        }
                                        if (isWallAtRight == false && isWallAtTop == false) {
                                                finaldirection = 2;
                                        }
                                }
                        }
                        if (game.ghosts[menekuljTole].getTileI() < game.pacmans[id].getTileI()) {
                                if (game.ghosts[menekuljTole].getTileJ() >= game.pacmans[id].getTileJ()) {
                                        if (isWallAtLeft) {
                                                finaldirection = 2;
                                        }
                                        if (isWallAtBottom) {
                                                finaldirection = 1;
                                        }
                                        if (isWallAtRight == false && isWallAtTop == false) {
                                                finaldirection = 1;
                                        }
                                }
                        }
						
						 if (game.ghosts[menekuljTole].getTileI() > game.pacmans[id].getTileI() &&game.ghosts[menekuljTole].getTileJ() == game.pacmans[id].getTileJ()) {
											    finaldirection = 0;
										 }
                        if (isWallAtRight && isWallAtTop) { //sarkos falak eseten
                                if (3 == previousDirection || 3 == previousSecondDirection) {
                                        finaldirection = 2;
                                } else
                                if (0 == previousDirection || 0 == previousSecondDirection) {
                                        finaldirection = 1;
                                }
                        } else
                        if (isWallAtRight && isWallAtBottom) {
                                if (2 == previousDirection || 2 == previousSecondDirection) {
                                        finaldirection = 1;
                                } else
                                if (3 == previousDirection || 3 == previousSecondDirection) {
                                        finaldirection = 0;
                                }
                        } else
                        if (isWallAtLeft && isWallAtTop) {
                                if (1 == previousDirection || 1 == previousSecondDirection) {
                                        finaldirection = 2;
                                } else
                                if (0 == previousDirection || 0 == previousSecondDirection) {
                                        finaldirection = 3;
                                }
                        } else
                        if (isWallAtLeft && isWallAtBottom) {
                                if (1 == previousDirection || 1 == previousSecondDirection) {
                                        finaldirection = 0;
                                } else
                                if (2 == previousDirection || 2 == previousSecondDirection) {
                                        finaldirection = 3;
                                }
                        } else //ha pedig egy fal van megis arra akar menni
                                if (isWallAtRight && (3 == previousDirection || 3 == previousSecondDirection)) {
                                        finaldirection = 0;
                                } else
                        if (isWallAtTop && (0 == previousDirection || 0 == previousSecondDirection)) {
                                finaldirection = 1;
                        } else
                        if (isWallAtLeft && (1 == previousDirection || 1 == previousSecondDirection)) {
                                finaldirection = 0;
                        } else
                        if (isWallAtBottom && (2 == previousDirection || 2 == previousSecondDirection)) {
                                finaldirection = 1;
                        }
						if (game.pacmans[id].getTileI()==20 && game.pacmans[id].getTileJ()==15){
							finaldirection=3;
						}
                        previousSecondDirection = previousDirection;
                        previousDirection = game.pacmans[id].getDirection();
                        return finaldirection;
                } else
                if (vegsoSzellem == 5) { //ez az ha nem kell menekulni es nem is eheto a szellem 
									if (game.pacmans[id].getTileI()==5 && game.pacmans[id].getTileJ()==9){
							finaldirection=1;
						}						
                        previousSecondDirection = previousDirection;
                        previousDirection = game.pacmans[id].getDirection();
                        return finaldirection;
                }
				else {
					
                        if (game.ghosts[vegsoSzellem].getTileI() >= game.pacmans[id].getTileI()) { //ez pedig ha eheto
                                if (game.ghosts[vegsoSzellem].getTileJ() > game.pacmans[id].getTileJ()) { //ugyanaz mint a menekulesnel csak ellentetes iranyokkal
                                        if (isWallAtRight) {
                                                finaldirection = 2;
                                        }
                                        if (isWallAtBottom) {
                                                finaldirection = 3;
                                        } else if (isWallAtRight == false && isWallAtRight == false) {
                                                finaldirection = 2;
                                        }
                                }
                        }
                        if (game.ghosts[vegsoSzellem].getTileI() >= game.pacmans[id].getTileI()) {
                                if (game.ghosts[vegsoSzellem].getTileJ() < game.pacmans[id].getTileJ()) {
                                        if (isWallAtLeft) {
                                                finaldirection = 2;
                                        }
                                        if (isWallAtBottom) {
                                                finaldirection = 1;
                                        } else if (isWallAtLeft == false && isWallAtLeft == false) {
                                                finaldirection = 1;
                                        }
                                }
                        }
                        if (game.ghosts[vegsoSzellem].getTileI() < game.pacmans[id].getTileI()) {
                                if (game.ghosts[vegsoSzellem].getTileJ() <= game.pacmans[id].getTileJ()) {
                                        if (isWallAtLeft) {
                                                finaldirection = 0;
                                        }
                                        if (isWallAtTop) {
                                                finaldirection = 1;
                                        } else if (isWallAtLeft == false && isWallAtTop == false) {
                                                finaldirection = 0;
                                        }
                                }
                        }
                        if (game.ghosts[vegsoSzellem].getTileI() < game.pacmans[id].getTileI()) {
                                if (game.ghosts[vegsoSzellem].getTileJ() >= game.pacmans[id].getTileJ()) {
                                        if (isWallAtRight) {
                                                finaldirection = 0;
                                        }
                                        if (isWallAtTop) {
                                                finaldirection = 3;
                                        } else if (isWallAtRight == false && isWallAtTop == false) {
                                                finaldirection = 3;
                                        }
                                }
                        }
                        if (isWallAtRight && isWallAtTop) { //sarkos falak eseten
                                if (3 == previousDirection || 3 == previousSecondDirection) {
                                        finaldirection = 2;
                                } else
                                if (0 == previousDirection || 0 == previousSecondDirection) {
                                        finaldirection = 1;
                                }
                        } else
                        if (isWallAtRight && isWallAtBottom) {
                                if (2 == previousDirection || 2 == previousSecondDirection) {
                                        finaldirection = 1;
                                } else
                                if (3 == previousDirection || 3 == previousSecondDirection) {
                                        finaldirection = 0;
                                }
                        } else
                        if (isWallAtLeft && isWallAtTop) {
                                if (1 == previousDirection || 1 == previousSecondDirection) {
                                        finaldirection = 2;
                                } else
                                if (0 == previousDirection || 0 == previousSecondDirection) {
                                        finaldirection = 3;
                                }
                        } else
                        if (isWallAtLeft && isWallAtBottom) {
                                if (1 == previousDirection || 1 == previousSecondDirection) {
                                        finaldirection = 0;
                                } else
                                if (2 == previousDirection || 2 == previousSecondDirection) {
                                        finaldirection = 3;
                                }
                        } else //egyeduli falak eseten
                                if (isWallAtRight && (3 == previousDirection || 3 == previousSecondDirection)) {
                                        finaldirection = 0;
                                } else
                        if (isWallAtTop && (0 == previousDirection || 0 == previousSecondDirection)) {
                                finaldirection = 1;
                        } else
                        if (isWallAtLeft && (1 == previousDirection || 1 == previousSecondDirection)) {
                                finaldirection = 0;
                        } else
                        if (isWallAtBottom && (2 == previousDirection || 2 == previousSecondDirection)) {
                                finaldirection = 1;
                        }
                        previousSecondDirection = previousDirection;
                        previousDirection = game.pacmans[id].getDirection();
                        return finaldirection;
                }
        }
}