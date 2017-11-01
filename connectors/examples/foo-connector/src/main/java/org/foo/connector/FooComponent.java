begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.foo.connector
package|package
name|org
operator|.
name|foo
operator|.
name|connector
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
name|component
operator|.
name|connector
operator|.
name|DefaultConnectorComponent
import|;
end_import

begin_class
DECL|class|FooComponent
specifier|public
class|class
name|FooComponent
extends|extends
name|DefaultConnectorComponent
block|{
DECL|method|FooComponent ()
specifier|public
name|FooComponent
parameter_list|()
block|{
name|super
argument_list|(
literal|"foo"
argument_list|,
literal|"org.foo.connector.FooComponent"
argument_list|)
expr_stmt|;
comment|// show how you can add a fixed header
name|setBeforeConsumer
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"whoami"
argument_list|,
literal|"I am foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

