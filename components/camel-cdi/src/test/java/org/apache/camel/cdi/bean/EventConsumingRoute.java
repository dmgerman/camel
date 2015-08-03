begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|ApplicationScoped
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|builder
operator|.
name|RouteBuilder
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
name|cdi
operator|.
name|CdiEventEndpoint
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
name|cdi
operator|.
name|pojo
operator|.
name|EventPayload
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
name|cdi
operator|.
name|qualifier
operator|.
name|BarQualifier
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
name|cdi
operator|.
name|qualifier
operator|.
name|FooQualifier
import|;
end_import

begin_class
annotation|@
name|ApplicationScoped
DECL|class|EventConsumingRoute
specifier|public
class|class
name|EventConsumingRoute
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Inject
DECL|field|objectCdiEventEndpoint
specifier|private
name|CdiEventEndpoint
argument_list|<
name|Object
argument_list|>
name|objectCdiEventEndpoint
decl_stmt|;
annotation|@
name|Inject
DECL|field|stringCdiEventEndpoint
specifier|private
name|CdiEventEndpoint
argument_list|<
name|String
argument_list|>
name|stringCdiEventEndpoint
decl_stmt|;
annotation|@
name|Inject
DECL|field|stringPayloadCdiEventEndpoint
specifier|private
name|CdiEventEndpoint
argument_list|<
name|EventPayload
argument_list|<
name|String
argument_list|>
argument_list|>
name|stringPayloadCdiEventEndpoint
decl_stmt|;
annotation|@
name|Inject
DECL|field|integerPayloadCdiEventEndpoint
specifier|private
name|CdiEventEndpoint
argument_list|<
name|EventPayload
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|integerPayloadCdiEventEndpoint
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|FooQualifier
DECL|field|fooQualifierCdiEventEndpoint
specifier|private
name|CdiEventEndpoint
argument_list|<
name|Long
argument_list|>
name|fooQualifierCdiEventEndpoint
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|BarQualifier
DECL|field|barQualifierCdiEventEndpoint
specifier|private
name|CdiEventEndpoint
argument_list|<
name|Long
argument_list|>
name|barQualifierCdiEventEndpoint
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
name|objectCdiEventEndpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumeObject"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|stringCdiEventEndpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumeString"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|stringPayloadCdiEventEndpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumeStringPayload"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|integerPayloadCdiEventEndpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumeIntegerPayload"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|fooQualifierCdiEventEndpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumeFooQualifier"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|barQualifierCdiEventEndpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumeBarQualifier"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

