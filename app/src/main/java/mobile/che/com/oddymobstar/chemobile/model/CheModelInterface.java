package mobile.che.com.oddymobstar.chemobile.model;

import message.CoreMessage;

/**
 * Created by timmytime on 30/01/16.
 */
public interface CheModelInterface {

    //we dont want the mobile variants on the core schema (re android cursors) and we dont want the schema models here (re java8 syntax)
     void create(CoreMessage coreMessage);
     CoreMessage getMessage();
}
