begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

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
name|Exchange
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
name|spring
operator|.
name|spi
operator|.
name|SpringTransactionPolicy
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionTemplate
import|;
end_import

begin_comment
comment|/**  * An extension of the {@link RouteBuilder} to provide some additional helper methods  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|SpringRouteBuilder
specifier|public
specifier|abstract
class|class
name|SpringRouteBuilder
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|RouteBuilder
argument_list|<
name|E
argument_list|>
block|{
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
comment|/**      * Configures a transaction interceptor on routes created by this builder using the named spring bean      * for the {@link TransactionTemplate} to use for the transaction      *      * @param transactionTemplateName the name of the spring bean in the application context which is the      *                                {@link TransactionTemplate} to use      * @return this builder      */
DECL|method|transactionInterceptor (String transactionTemplateName)
specifier|public
name|SpringRouteBuilder
argument_list|<
name|E
argument_list|>
name|transactionInterceptor
parameter_list|(
name|String
name|transactionTemplateName
parameter_list|)
block|{
name|TransactionTemplate
name|template
init|=
name|bean
argument_list|(
name|TransactionTemplate
operator|.
name|class
argument_list|,
name|transactionTemplateName
argument_list|)
decl_stmt|;
name|setTransactionPolicy
argument_list|(
operator|new
name|SpringTransactionPolicy
argument_list|(
name|template
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Looks up the bean with the given name in the application context and returns it, or throws an exception if the      * bean is not present or is not of the given type      *      * @param type     the type of the bean      * @param beanName the name of the bean in the application context      * @return the bean      */
DECL|method|bean (Class<T> type, String beanName)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|bean
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|beanName
parameter_list|)
block|{
name|ApplicationContext
name|context
init|=
name|getApplicationContext
argument_list|()
decl_stmt|;
return|return
operator|(
name|T
operator|)
name|context
operator|.
name|getBean
argument_list|(
name|beanName
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Looks up the bean with the given type in the application context and returns it, or throws an exception if the      * bean is not present or there are multiple possible beans to choose from for the given type      *      * @param type the type of the bean      * @return the bean      */
DECL|method|bean (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|bean
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|ApplicationContext
name|context
init|=
name|getApplicationContext
argument_list|()
decl_stmt|;
name|String
index|[]
name|names
init|=
name|context
operator|.
name|getBeanNamesForType
argument_list|(
name|type
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
condition|)
block|{
name|int
name|count
init|=
name|names
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|1
condition|)
block|{
comment|// lets instantiate the single bean
return|return
operator|(
name|T
operator|)
name|context
operator|.
name|getBean
argument_list|(
name|names
index|[
literal|0
index|]
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|count
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Too many beans in the application context of type: "
operator|+
name|type
operator|+
literal|". Found: "
operator|+
name|count
argument_list|)
throw|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No bean available in the application context of type: "
operator|+
name|type
argument_list|)
throw|;
block|}
comment|/**      * Returns the application context which has been configured via the {@link #setApplicationContext(ApplicationContext)}      * method  or from the underlying {@link SpringCamelContext}      *       * @return      */
DECL|method|getApplicationContext ()
specifier|public
name|ApplicationContext
name|getApplicationContext
parameter_list|()
block|{
if|if
condition|(
name|applicationContext
operator|==
literal|null
condition|)
block|{
name|CamelContext
name|camelContext
init|=
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContext
operator|instanceof
name|SpringCamelContext
condition|)
block|{
name|SpringCamelContext
name|springCamelContext
init|=
operator|(
name|SpringCamelContext
operator|)
name|camelContext
decl_stmt|;
return|return
name|springCamelContext
operator|.
name|getApplicationContext
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This SpringBuilder is not being used with a SpringCamelContext and there is no applicationContext property configured"
argument_list|)
throw|;
block|}
block|}
return|return
name|applicationContext
return|;
block|}
comment|/**      * Sets the application context to use to lookup beans      */
DECL|method|setApplicationContext (ApplicationContext applicationContext)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
block|}
end_class

end_unit

