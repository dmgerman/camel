begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|spi
operator|.
name|Required
import|;
end_import

begin_comment
comment|/**  * Abstract command for working with a single {@link org.apache.camel.CamelContext}  */
end_comment

begin_class
DECL|class|AbstractContextCommand
specifier|public
specifier|abstract
class|class
name|AbstractContextCommand
extends|extends
name|AbstractCamelCommand
block|{
comment|/**      * The name of the Camel context.      */
annotation|@
name|Required
DECL|method|getContext ()
specifier|public
specifier|abstract
name|String
name|getContext
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|execute (CamelController camelController, PrintStream out, PrintStream err)
specifier|public
name|Object
name|execute
parameter_list|(
name|CamelController
name|camelController
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|camelController
operator|.
name|getCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
name|err
operator|.
name|println
argument_list|(
literal|"Camel context "
operator|+
name|getContext
argument_list|()
operator|+
literal|" not found."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// Setting thread context classloader to the bundle classloader to enable legacy code that relies on it
name|ClassLoader
name|oldClassloader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|camelContext
operator|.
name|getApplicationContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|performContextCommand
argument_list|(
name|camelContext
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|oldClassloader
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Perform Context-specific command      *      * @param camelContext non-null {@link CamelContext}      * @param out          the output printer stream      * @param err          the error print stream      */
DECL|method|performContextCommand (CamelContext camelContext, PrintStream out, PrintStream err)
specifier|protected
specifier|abstract
name|void
name|performContextCommand
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

