begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pojo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pojo
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
name|CamelContext
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
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link PojoEndpoint}.  It holds the  * list of named pojos that queue endpoints reference.  *  * @version $Revision: 519973 $  */
end_comment

begin_class
DECL|class|PojoComponent
specifier|public
class|class
name|PojoComponent
implements|implements
name|Component
argument_list|<
name|PojoExchange
argument_list|>
block|{
DECL|field|services
specifier|protected
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|services
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|consumers
specifier|protected
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|PojoConsumer
argument_list|>
name|consumers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PojoConsumer
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|container
specifier|private
name|CamelContext
name|container
decl_stmt|;
DECL|method|addService (String uri, Object pojo)
specifier|public
name|void
name|addService
parameter_list|(
name|String
name|uri
parameter_list|,
name|Object
name|pojo
parameter_list|)
block|{
name|services
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|pojo
argument_list|)
expr_stmt|;
block|}
DECL|method|removeService (String uri)
specifier|public
name|void
name|removeService
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|services
operator|.
name|remove
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|removeConsumer
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|getService (String uri)
specifier|public
name|Object
name|getService
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|services
operator|.
name|get
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|addConsumer (String uri, PojoConsumer endpoint)
name|void
name|addConsumer
parameter_list|(
name|String
name|uri
parameter_list|,
name|PojoConsumer
name|endpoint
parameter_list|)
block|{
name|consumers
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|removeConsumer (String uri)
name|void
name|removeConsumer
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|consumers
operator|.
name|remove
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|getConsumer (String uri)
specifier|public
name|PojoConsumer
name|getConsumer
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|consumers
operator|.
name|get
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|setCamelContext (CamelContext container)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|container
parameter_list|)
block|{
name|this
operator|.
name|container
operator|=
name|container
expr_stmt|;
block|}
DECL|method|getContainer ()
specifier|public
name|CamelContext
name|getContainer
parameter_list|()
block|{
return|return
name|container
return|;
block|}
block|}
end_class

end_unit

