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
    void AddPrecondition(GeneralDObject odk) throws ValidationException;
    void UpdatePrecondition(GeneralDObject odk) throws ValidationException;
    void DeletePrecondition(GeneralDObject odk) throws ValidationException;
}
