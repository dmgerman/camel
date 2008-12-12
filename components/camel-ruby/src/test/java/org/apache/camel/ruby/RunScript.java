begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.ruby
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ruby
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
name|org
operator|.
name|jruby
operator|.
name|Main
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|RunScript
specifier|public
specifier|final
class|class
name|RunScript
block|{
DECL|method|RunScript ()
specifier|private
name|RunScript
parameter_list|()
block|{
comment|// helper class
block|}
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|runScript
argument_list|(
literal|"src/test/java/org/apache/camel/ruby/example.rb"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|String
name|arg
range|:
name|args
control|)
block|{
name|runScript
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|runScript (String name)
specifier|public
specifier|static
name|void
name|runScript
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
index|[]
name|args
init|=
block|{
name|name
block|}
decl_stmt|;
name|Main
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

