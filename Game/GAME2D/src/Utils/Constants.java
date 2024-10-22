package Utils;

public class Constants {


    public static class PlayerConstants{
        public static final int LEFT = 1;
        public static final int RIGHT = 2;
        public static final int IDLE = 0;
        public static final int DOWN = 3;
//manejo de contabilidad de sprites en caso de agregar un sprite complejo o de mas de 4 frames
        public static int GetSpriteAmount(int player_action)
        {
            switch(player_action)
            {
                case LEFT:
                    return 4;
                case RIGHT:
                    return 4;
                case IDLE:
                    return 4;
                case DOWN:
                    return 4;
                default:
                    return 4;
            }
        }
    }
}
