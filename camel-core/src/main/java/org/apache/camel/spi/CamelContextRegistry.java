begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultCamelContextRegistry
import|;
end_import

begin_comment
comment|/**  * A global registry for camel contexts.  *  * The runtime registeres all contexts that derive from {@link DefaultCamelContext} automatically.  */
end_comment

begin_interface
DECL|interface|CamelContextRegistry
specifier|public
interface|interface
name|CamelContextRegistry
block|{
comment|/** 	 * The registry singleton 	 */
DECL|field|INSTANCE
specifier|static
specifier|final
name|CamelContextRegistry
name|INSTANCE
init|=
operator|new
name|DefaultCamelContextRegistry
argument_list|()
decl_stmt|;
comment|/** 	 * A listener that can be registered witht he registry 	 */
DECL|class|Listener
specifier|public
class|class
name|Listener
block|{
comment|/** 		 * Called when a context is added to the registry 		 */
DECL|method|contextAdded (CamelContext camelContext)
specifier|public
name|void
name|contextAdded
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{ 		}
comment|/** 		 * Called when a context is removed from the registry 		 */
DECL|method|contextRemoved (CamelContext camelContext)
specifier|public
name|void
name|contextRemoved
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{ 		}
block|}
comment|/** 	 * Add the given listener to the registry 	 * @param withCallback If true, the given listener is called with the set of already registered contexts 	 */
DECL|method|addListener (Listener listener, boolean withCallback)
name|void
name|addListener
parameter_list|(
name|Listener
name|listener
parameter_list|,
name|boolean
name|withCallback
parameter_list|)
function_decl|;
comment|/** 	 * Remove the given listener from the registry 	 * @param withCallback If true, the given listener is called with the set of already registered contexts 	 */
DECL|method|removeListener (Listener listener, boolean withCallback)
name|void
name|removeListener
parameter_list|(
name|Listener
name|listener
parameter_list|,
name|boolean
name|withCallback
parameter_list|)
function_decl|;
comment|/** 	 * Get the set of registerd contexts 	 */
DECL|method|getContexts ()
name|Set
argument_list|<
name|CamelContext
argument_list|>
name|getContexts
parameter_list|()
function_decl|;
comment|/** 	 * Get the set of registered contexts for the given name. 	 *  	 * Because the camel context name property is neither unique nor immutable 	 * the returned set may vary for the same name. 	 */
DECL|method|getContexts (String name)
name|Set
argument_list|<
name|CamelContext
argument_list|>
name|getContexts
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/** 	 * Get the registered context for the given name. 	 *  	 * @return The first context in the set 	 * @throws IllegalStateException when there is no registered context for the given name 	 * @see CamelContextRegistry#getContexts(String) 	 */
DECL|method|getRequiredContext (String name)
name|CamelContext
name|getRequiredContext
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/** 	 * Get the registered context for the given name. 	 *  	 * @return The first context in the set or null 	 * @see CamelContextRegistry#getContexts(String) 	 */
DECL|method|getContext (String name)
name|CamelContext
name|getContext
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

