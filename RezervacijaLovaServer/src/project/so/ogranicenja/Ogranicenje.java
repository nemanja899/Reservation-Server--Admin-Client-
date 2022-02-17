/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.ogranicenja;

import domain.GeneralDObject;
import validation.ValidationException;

/**
 *
 * @author User
 */
public interface Ogranicenje {
    void addPrecondition(GeneralDObject odk) throws ValidationException;
    void updatePrecondition(GeneralDObject odk) throws ValidationException;
    void deletePrecondition(GeneralDObject odk) throws ValidationException;
}
