begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|swagger
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|SwaggerRestProducerFactoryTest
specifier|public
class|class
name|SwaggerRestProducerFactoryTest
block|{
annotation|@
name|Test
DECL|method|shouldLoadSwaggerPetStoreModel ()
specifier|public
name|void
name|shouldLoadSwaggerPetStoreModel
parameter_list|()
throws|throws
name|Exception
block|{
name|SwaggerRestProducerFactory
name|factory
init|=
operator|new
name|SwaggerRestProducerFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|loadSwaggerModel
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|,
literal|"petstore.json"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

