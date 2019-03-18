begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.remoting
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|remoting
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
name|Produce
import|;
end_import

begin_class
DECL|class|MyMultiArgument
specifier|public
class|class
name|MyMultiArgument
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:myargs"
argument_list|,
name|binding
operator|=
literal|false
argument_list|)
DECL|field|myMultiArgumentServiceInterface
name|MyMultiArgumentServiceInterface
name|myMultiArgumentServiceInterface
decl_stmt|;
DECL|method|doSomethingMultiple ()
specifier|public
name|void
name|doSomethingMultiple
parameter_list|()
block|{
name|myMultiArgumentServiceInterface
operator|.
name|doSomething
argument_list|(
literal|"Hello World 1"
argument_list|,
literal|"Hello World 2"
argument_list|,
operator|new
name|Long
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

