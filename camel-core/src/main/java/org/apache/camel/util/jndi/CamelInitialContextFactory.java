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

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|spi
operator|.
name|InitialContextFactory
import|;
end_import

begin_comment
comment|/**  * A factory of the Camel InitialContext which allows a Map to be used to create a  * JNDI context.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelInitialContextFactory
specifier|public
class|class
name|CamelInitialContextFactory
implements|implements
name|InitialContextFactory
block|{
comment|/**      * Creates a new context with the given environment.      *      * @param  environment  the environment, must not be<tt>null</tt>      * @return the created context.      * @throws NamingException is thrown if creation failed.      */
DECL|method|getInitialContext (Hashtable environment)
specifier|public
name|Context
name|getInitialContext
parameter_list|(
name|Hashtable
name|environment
parameter_list|)
throws|throws
name|NamingException
block|{
try|try
block|{
return|return
operator|new
name|JndiContext
argument_list|(
name|environment
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|NamingException
condition|)
block|{
throw|throw
operator|(
name|NamingException
operator|)
name|e
throw|;
block|}
name|NamingException
name|exception
init|=
operator|new
name|NamingException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|exception
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|exception
throw|;
block|}
block|}
block|}
end_class

end_unit

