package com.straddle.abstrct;

import com.straddle.dataobject.IRootObject;
import com.straddle.serviceInterface.IBasicService;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BasicService implements IBasicService {

    public abstract IRootObject main(IRootObject rootObject) throws Throwable;

    public void execute(IRootObject rootObject) {
        try {
            main(rootObject);
        } catch (Throwable throwable) {
            Logger.getLogger("yoLogger").log(Level.ALL,"EXECUTION CLASS IS:" + this.getClass() , throwable);
            throwable.printStackTrace();
        }
    }
}
