begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.jndi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|jndi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_comment
comment|/**  * A factory of the Camel {@link javax.naming.InitialContext} which allows a {@link java.util.Map} to be used to create a  * JNDI context.  *<p/>  * This implementation is singleton based, by creating a<b>new</b> context once, and reusing it on each call to  * {@link #getInitialContext(java.util.Hashtable)}.  *  * @version  */
end_comment

begin_class
DECL|class|CamelSingletonInitialContextFactory
specifier|public
class|class
name|CamelSingletonInitialContextFactory
extends|extends
name|CamelInitialContextFactory
block|{
DECL|field|context
specifier|private
specifier|static
specifier|volatile
name|Context
name|context
decl_stmt|;
comment|/**      * Gets or creates the context with the given environment.      *<p/>      * This implementation will create the context once, and then return the same instance      * on multiple calls.      *      * @param  environment  the environment, must not be<tt>null</tt>      * @return the created context.      * @throws javax.naming.NamingException is thrown if creation failed.      */
DECL|method|getInitialContext (Hashtable<?, ?> environment)
specifier|public
specifier|synchronized
name|Context
name|getInitialContext
parameter_list|(
name|Hashtable
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|environment
parameter_list|)
throws|throws
name|NamingException
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
name|super
operator|.
name|getInitialContext
argument_list|(
name|environment
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

