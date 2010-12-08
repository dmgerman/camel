begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|blueprint
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
name|EndpointInject
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

begin_class
DECL|class|TestProducer
specifier|public
class|class
name|TestProducer
block|{
annotation|@
name|EndpointInject
argument_list|(
name|ref
operator|=
literal|"testEndpoint"
argument_list|)
DECL|field|testEndpoint
specifier|private
name|ProducerTemplate
name|testEndpoint
decl_stmt|;
DECL|method|getTestEndpoint ()
specifier|public
name|ProducerTemplate
name|getTestEndpoint
parameter_list|()
block|{
return|return
name|testEndpoint
return|;
block|}
block|}
end_class

end_unit

