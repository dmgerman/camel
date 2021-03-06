begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.postprocessor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|postprocessor
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ProducerTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"testPojo"
argument_list|)
DECL|class|TestPojo
specifier|public
class|class
name|TestPojo
block|{
annotation|@
name|MagicAnnotation
argument_list|(
literal|"Changed Value"
argument_list|)
DECL|field|testValue
specifier|private
name|String
name|testValue
init|=
literal|"Initial Value"
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"mock:foo"
argument_list|)
DECL|field|producer
specifier|private
name|ProducerTemplate
name|producer
decl_stmt|;
DECL|method|getTestValue ()
specifier|public
name|String
name|getTestValue
parameter_list|()
block|{
return|return
name|this
operator|.
name|testValue
return|;
block|}
DECL|method|sendToFoo (String msg)
specifier|public
name|void
name|sendToFoo
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|producer
operator|.
name|sendBody
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

