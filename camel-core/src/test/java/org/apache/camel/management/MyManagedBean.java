begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"My Managed Bean within Camel"
argument_list|)
DECL|class|MyManagedBean
specifier|public
class|class
name|MyManagedBean
block|{
DECL|field|camelsSeenCount
specifier|private
name|int
name|camelsSeenCount
decl_stmt|;
DECL|method|doSomething (String body)
specifier|public
name|String
name|doSomething
parameter_list|(
name|String
name|body
parameter_list|)
block|{
if|if
condition|(
name|body
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
condition|)
block|{
name|camelsSeenCount
operator|++
expr_stmt|;
block|}
return|return
literal|"Managed "
operator|+
name|body
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"How many Camels Have been Seen"
argument_list|)
DECL|method|getCamelsSeenCount ()
specifier|public
name|int
name|getCamelsSeenCount
parameter_list|()
block|{
return|return
name|camelsSeenCount
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Set Camels Seen Count to Zero"
argument_list|)
DECL|method|resetCamelsSeenCount ()
specifier|public
name|void
name|resetCamelsSeenCount
parameter_list|()
block|{
name|camelsSeenCount
operator|=
literal|0
expr_stmt|;
block|}
block|}
end_class

end_unit

