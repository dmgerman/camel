begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.firebase.exception
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|firebase
operator|.
name|exception
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|firebase
operator|.
name|database
operator|.
name|DatabaseError
import|;
end_import

begin_comment
comment|/**  * Used to mark an exception occurred in the Firebase Camel processor.  */
end_comment

begin_class
DECL|class|FirebaseException
specifier|public
class|class
name|FirebaseException
extends|extends
name|RuntimeException
block|{
DECL|field|databaseError
specifier|private
name|DatabaseError
name|databaseError
decl_stmt|;
comment|/**      * Constructs a new runtime exception with {@code null} as its      * detail message.  The cause is not initialized, and may subsequently be      * initialized by a call to {@link #initCause}.      */
DECL|method|FirebaseException ()
specifier|public
name|FirebaseException
parameter_list|()
block|{     }
comment|/**      * Constructs a new runtime exception with the specified detail message.      * The cause is not initialized, and may subsequently be initialized by a      * call to {@link #initCause}.      *      * @param message the detail message. The detail message is saved for      *                later retrieval by the {@link #getMessage()} method.      */
DECL|method|FirebaseException (String message)
specifier|public
name|FirebaseException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructs a new runtime exception with the specified detail message and      * cause.<p>Note that the detail message associated with      * {@code cause} is<i>not</i> automatically incorporated in      * this runtime exception's detail message.      *      * @param message the detail message (which is saved for later retrieval      *                by the {@link #getMessage()} method).      * @param cause   the cause (which is saved for later retrieval by the      *                {@link #getCause()} method).  (A<tt>null</tt> value is      *                permitted, and indicates that the cause is nonexistent or      *                unknown.)      * @since 1.4      */
DECL|method|FirebaseException (String message, Throwable cause)
specifier|public
name|FirebaseException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|setDatabaseError (DatabaseError databaseError)
specifier|public
name|void
name|setDatabaseError
parameter_list|(
name|DatabaseError
name|databaseError
parameter_list|)
block|{
name|this
operator|.
name|databaseError
operator|=
name|databaseError
expr_stmt|;
block|}
DECL|method|getDatabaseError ()
specifier|public
name|DatabaseError
name|getDatabaseError
parameter_list|()
block|{
return|return
name|databaseError
return|;
block|}
block|}
end_class

end_unit

