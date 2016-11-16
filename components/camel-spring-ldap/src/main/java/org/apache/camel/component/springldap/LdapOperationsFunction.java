begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.springldap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|springldap
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ldap
operator|.
name|core
operator|.
name|LdapOperations
import|;
end_import

begin_comment
comment|/**  * Provides a way to invoke any method on {@link LdapOperations} when an operation is not provided out of the box by this component.  *   * @param<Q>  *            - The set of request parameters as expected by the method being invoked  * @param<S>  *            - The response to be returned by the method being invoked  */
end_comment

begin_interface
DECL|interface|LdapOperationsFunction
specifier|public
interface|interface
name|LdapOperationsFunction
parameter_list|<
name|Q
parameter_list|,
name|S
parameter_list|>
block|{
comment|/** 	 * @param ldapOperations 	 *            - An instance of {@link LdapOperations} 	 * @param request 	 *            - Any object needed by the {@link LdapOperations} method being invoked 	 * @return - result of the {@link LdapOperations} method being invoked 	 */
DECL|method|apply (LdapOperations ldapOperations, Q request)
name|S
name|apply
parameter_list|(
name|LdapOperations
name|ldapOperations
parameter_list|,
name|Q
name|request
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

