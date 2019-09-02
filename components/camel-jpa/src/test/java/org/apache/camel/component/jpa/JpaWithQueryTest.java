begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jpa
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
name|examples
operator|.
name|MultiSteps
import|;
end_import

begin_class
DECL|class|JpaWithQueryTest
specifier|public
class|class
name|JpaWithQueryTest
extends|extends
name|JpaWithNamedQueryTest
block|{
annotation|@
name|Override
DECL|method|assertURIQueryOption (JpaConsumer jpaConsumer)
specifier|protected
name|void
name|assertURIQueryOption
parameter_list|(
name|JpaConsumer
name|jpaConsumer
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"select o from "
operator|+
name|entityName
operator|+
literal|" o where o.step = 1"
argument_list|,
name|jpaConsumer
operator|.
name|getQuery
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpointUri ()
specifier|protected
name|String
name|getEndpointUri
parameter_list|()
block|{
return|return
literal|"jpa://"
operator|+
name|MultiSteps
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?query=select o from "
operator|+
name|entityName
operator|+
literal|" o where o.step = 1"
return|;
block|}
block|}
end_class

end_unit

