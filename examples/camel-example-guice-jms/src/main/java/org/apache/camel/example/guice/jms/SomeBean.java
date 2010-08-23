begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.guice.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|guice
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_comment
comment|/**  * @version $Revision$ */
end_comment

begin_class
DECL|class|SomeBean
specifier|public
class|class
name|SomeBean
block|{
annotation|@
name|Inject
DECL|field|printer
specifier|private
name|Printer
name|printer
decl_stmt|;
DECL|method|someMethod (String body)
specifier|public
name|void
name|someMethod
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|printer
operator|.
name|print
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"SomeBean"
return|;
block|}
block|}
end_class

end_unit

