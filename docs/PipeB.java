package com.szakacs.kpi.fei.tuke.game.player;

import com.szakacs.kpi.fei.tuke.game.arena.AbstractPlayer;
import com.szakacs.kpi.fei.tuke.game.arena.TreasureScooper;



public class PipeB extends AbstractPlayer {

    // Tu si mozes vytvorit vlastne clenske premenne pre pouzitie pocas hry
    // priklad je uvedeny tu:
    private TreasureScooper world;    


    // Jeden sposob ako riesit ulohy, kde treba rozne spravanie od hraca v zavislosti od toho, 
    // v akej je situacii, bez toho, aby sa programator zamotal do nekonecneho viru podmienok
    // a pomocnych premennych opisujucich aktualny stav alebo ulohu hraca, je pomocou mechanizmu 
    // state machine, ktory je v Jave riesitelny pomocou enumeracnych typov, priklad je uvedeny 
    // nizsie a priklad ich vyuzitia je uvedeny v metode act
    
    private enum State{
        CLEARLEFT,    // stav ked hrac zbiera vsetky kusy zlata v smere vlavo od miesta, odkial prisiel 
        CLEARRIGHT,   // stav ked hrac zbiera vsetky kusy zlata v smere vpravo od miesta, odkial prisiel
        BEGIN,        // stav, kedy vchadza do tunela, kde bude zbierat kusy zlata najprv vlavo, potom vpravo
        RETURN,       // stav, ked sa hrac vracia, odkial prisiel
        FINISH        // vsetko hotove
    }
    private State playerState;
    
    public PipeB(TreasureScooper world) {
        super(world);
        this.world = world;
        this.playerState = State.BEGIN;
    }

    // Tato metoda sa zavola v kazdej iteracii herneho cyklu, tu je  implementuje student
    @Override
    protected void act() {
        switch (this.playerState){
            case CLEARLEFT:
                // zbieraj vsetko vlavo od vchodu do tunela
                if ( /*vsetky kusy zlata vlavo pozbierane*/ )
                    this.playerState = State.RETURN;
                break;
            case CLEARRIGHT:
                // zbieraj vsetko vpravo od vchodu do tunela
                if ( /*vsetky kusy zlata vlavo pozbierane*/ )
                    this.playerState = State.RETURN;
                break;
            case BEGIN:
                // chod dole, kym nie si v tuneli
                if ( /* som v tuneli */ )
                    this.playerState = State.CLEARLEFT;
                break;
            case RETURN:
                // vrat sa na povodnu poziciu pri vchode do tunela
                if (/* vsetko vlavo ale nie vpravo pozbierane*/)
                    this.playerState = State.CLEARRIGHT;
                else  // vsetko pozbierane
                    this.playerState = State.FINISH
                break;
            case FINISH:
                // nerob nic
                break;
            // inak, kod pre kazdy case moze byt aj v samostatnych metodach.
        }
    }
}
