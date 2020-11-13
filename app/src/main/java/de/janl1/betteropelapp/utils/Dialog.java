package de.janl1.betteropelapp.utils;

import android.app.AlertDialog;
import android.content.Context;

public class Dialog {

    public static AlertDialog showErrorMessage(Context context, String errorMessage, String detailedMessage)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Oops... | Fehler");
        builder.setMessage("Bei der Ausführung der Aktion ist ein Fehler aufgetreten!\n\nFehler: " + errorMessage + "\n\nFehlermeldung:\n\n" + detailedMessage);
        return builder.create();
    }
}
