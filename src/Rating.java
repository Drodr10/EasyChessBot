public class Rating {
    static final int pawnBoard[][]={//attribute to http://chessprogramming.wikispaces.com/Simplified+evaluation+function
        { 0,  0,  0,  0,  0,  0,  0,  0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        { 5,  5, 10, 25, 25, 10,  5,  5},
        { 0,  0,  0, 20, 20,  0,  0,  0},
        { 5, -5,-10,  0,  0,-10, -5,  5},
        { 5, 10, 10,-20,-20, 10, 10,  5},
        { 0,  0,  0,  0,  0,  0,  0,  0}};
    static final int rookBoard[][]={
        { 0,  0,  0,  0,  0,  0,  0,  0},
        { 5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        { 0,  0,  0,  5,  5,  0,  0,  0}};
    static final int knightBoard[][]={
        {-50,-40,-30,-30,-30,-30,-40,-50},
        {-40,-20,  0,  0,  0,  0,-20,-40},
        {-30,  0, 10, 15, 15, 10,  0,-30},
        {-30,  5, 15, 20, 20, 15,  5,-30},
        {-30,  0, 15, 20, 20, 15,  0,-30},
        {-30,  5, 10, 15, 15, 10,  5,-30},
        {-40,-20,  0,  5,  5,  0,-20,-40},
        {-50,-40,-30,-30,-30,-30,-40,-50}};
    static final int bishopBoard[][]={
        {-20,-10,-10,-10,-10,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 10, 10, 10, 10,  0,-10},
        {-10, 10, 10, 10, 10, 10, 10,-10},
        {-10,  5,  0,  0,  0,  0,  5,-10},
        {-20,-10,-10,-10,-10,-10,-10,-20}};
    static final int queenBoard[][]={
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        { -5,  0,  5,  5,  5,  5,  0, -5},
        {  0,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  0,  0,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}};
    static final int kingMidBoard[][]={
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-20,-30,-30,-40,-40,-30,-30,-20},
        {-10,-20,-20,-20,-20,-20,-20,-10},
        { 20, 20,  0,  0,  0,  0, 20, 20},
        { 20, 30, 10,  0,  0, 10, 30, 20}};
    static final int kingEndBoard[][]={
        {-50,-40,-30,-20,-20,-30,-40,-50},
        {-30,-20,-10,  0,  0,-10,-20,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-30,  0,  0,  0,  0,-30,-30},
        {-50,-30,-30,-30,-30,-30,-30,-50}};
    static final int CHECKMATEORSTALEMATE = 0;

    public static int rating(int length, int depth){
        int material = rateMaterial();
        int finalRating = rateAttack()+material+rateMovability(length, depth, material)+ratePosition(material);
        Chess.flipBoard();
        material = rateMaterial();
        finalRating -= (rateAttack()+material+rateMovability(length, depth, material)+ratePosition(material));
        Chess.flipBoard();
        return -(finalRating+depth*50);
    }
    public static int rateAttack(){
        int attackRating = 0;
        int realPosition = Chess.whiteKingPosition;
        for (int i = 0; i < 64; i++) {
            switch (Chess.board[i/8][i%8]) {
                case "P":
                    Chess.whiteKingPosition = i;
                    if (!Chess.kingSafe()) {
                        attackRating-=64;
                    }  
                    break;
                case "R":
                    Chess.whiteKingPosition = i;
                    if (!Chess.kingSafe()) {
                        attackRating-=500;
                    }  
                    break;
                case "K":
                case "B":
                    Chess.whiteKingPosition = i;
                    if (!Chess.kingSafe()) {
                        attackRating-=300;
                    }  
                    break;
                case "Q":
                    Chess.whiteKingPosition = i;
                    if (!Chess.kingSafe()) {
                        attackRating-=900;
                    }  
                    break;
                default:
                    break;
            }
        }
        Chess.whiteKingPosition = realPosition;
        if (!Chess.kingSafe()) {
            attackRating-=200;
        }
        return attackRating/2;
    }
    public static int rateMaterial(){
        int materialRating = 0;
        int bishopRating = 0;
        for (int i = 0; i < 64; i++) {
            switch (Chess.board[i/8][i%8]) {
                case "P":  
                    materialRating+=100;
                    break;
                case "R":
                    materialRating+=500;
                    break;
                case "K":
                    materialRating+=300;
                    break;
                case "B":
                    bishopRating++;
                    break;
                case "Q":
                    materialRating+=900;
                    break;
                default:
                    break;
            }
        }
        if (bishopRating>=2) {
            materialRating+=300*bishopRating;
        } else if (bishopRating == 1) {
            materialRating+=250;
        }
        return materialRating;
    }
    public static int rateMovability(int length, int depth, int material){
        int movabilityRating = 0;
        movabilityRating+=length;
        if (length==CHECKMATEORSTALEMATE) {
            if (!Chess.kingSafe()) {
                movabilityRating -= 100000*depth;
            } else{
                movabilityRating -= 50000*depth;
            }
        }
        return movabilityRating;
    }
    public static int ratePosition(int material){
        int positionRating = 0;
        for (int i = 0; i < 64; i++) {
            switch (Chess.board[i/8][i%8]) {
                case "P":  
                    positionRating+=pawnBoard[i/8][i%8];
                    break;
                case "R":
                    positionRating+=rookBoard[i/8][i%8];
                    break;
                case "K":
                    positionRating+=knightBoard[i/8][i%8];
                    break;
                case "B":
                    positionRating+=bishopBoard[i/8][i%8];                    break;
                case "Q":
                    positionRating+=queenBoard[i/8][i%8];
                    break;
                case "A":
                    positionRating+=(isEndgame(material))?
                    kingEndBoard[i/8][i%8]+Chess.possibleA(Chess.whiteKingPosition).length()*30:
                    kingMidBoard[i/8][i%8]+Chess.possibleA(Chess.whiteKingPosition).length()*10;
                    break;
                default:
                    break;
            }
        }
        return positionRating;
    }

    public static boolean isEndgame(int material){
        return (material<1750);
    }

}
