begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * Script test helper.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ScriptTestHelper
specifier|public
specifier|final
class|class
name|ScriptTestHelper
block|{
DECL|method|ScriptTestHelper ()
specifier|private
name|ScriptTestHelper
parameter_list|()
block|{     }
DECL|method|canRunTestOnThisPlatform ()
specifier|public
specifier|static
name|boolean
name|canRunTestOnThisPlatform
parameter_list|()
block|{
comment|// requires java 1.6
name|String
name|version
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.version"
argument_list|)
decl_stmt|;
name|String
index|[]
name|numbers
init|=
name|version
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
if|if
condition|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|numbers
index|[
literal|0
index|]
argument_list|)
operator|>
literal|1
operator|||
name|Integer
operator|.
name|valueOf
argument_list|(
name|numbers
index|[
literal|1
index|]
argument_list|)
operator|>
literal|5
condition|)
block|{
comment|// JDK 1.6 or newer (eg JDK 2.x)
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

