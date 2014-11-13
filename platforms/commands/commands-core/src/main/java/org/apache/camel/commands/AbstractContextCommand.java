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
DECL|field|context
name|String
name|context
decl_stmt|;
comment|/**      * @param context The name of the Camel context.      */
DECL|method|AbstractContextCommand (String context)
specifier|protected
name|AbstractContextCommand
parameter_list|(
name|String
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
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
name|context
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
name|context
operator|+
literal|" not found."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// Setting thread context classloader to the bundle classloader to enable legacy code that relies on it
comment|//        ClassLoader oldClassloader = Thread.currentThread().getContextClassLoader();
comment|//        Thread.currentThread().setContextClassLoader(camelContext.getApplicationContextClassLoader());
try|try
block|{
return|return
name|performContextCommand
argument_list|(
name|camelController
argument_list|,
name|camelContext
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
return|;
block|}
finally|finally
block|{
comment|//            Thread.currentThread().setContextClassLoader(oldClassloader);
block|}
block|}
comment|/**      * Perform Context-specific command      *      * @param camelController the Camel controller      * @param camelContext    non-null {@link CamelContext}      * @param out             the output printer stream      * @param err             the error print stream      * @return response from command, or<tt>null</tt> if nothing to return      * @throws Exception is thrown if error executing command      */
DECL|method|performContextCommand (CamelController camelController, CamelContext camelContext, PrintStream out, PrintStream err)
specifier|protected
specifier|abstract
name|Object
name|performContextCommand
parameter_list|(
name|CamelController
name|camelController
parameter_list|,
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

