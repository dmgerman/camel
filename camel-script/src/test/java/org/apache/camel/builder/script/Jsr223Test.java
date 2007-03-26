begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.script
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|script
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|script
operator|.
name|ScriptEngineManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|script
operator|.
name|ScriptEngineFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|Jsr223Test
specifier|public
class|class
name|Jsr223Test
extends|extends
name|TestCase
block|{
DECL|method|testLanguageNames ()
specifier|public
name|void
name|testLanguageNames
parameter_list|()
throws|throws
name|Exception
block|{
name|ScriptEngineManager
name|manager
init|=
operator|new
name|ScriptEngineManager
argument_list|()
decl_stmt|;
for|for
control|(
name|ScriptEngineFactory
name|factory
range|:
name|manager
operator|.
name|getEngineFactories
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Factory: "
operator|+
name|factory
operator|.
name|getNames
argument_list|()
operator|+
literal|" "
operator|+
name|factory
operator|.
name|getEngineName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

