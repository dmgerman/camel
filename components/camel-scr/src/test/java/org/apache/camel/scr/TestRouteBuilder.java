begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.scr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|scr
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
name|commons
operator|.
name|lang
operator|.
name|Validate
import|;
end_import

begin_class
DECL|class|TestRouteBuilder
specifier|public
class|class
name|TestRouteBuilder
extends|extends
name|RouteBuilder
block|{
comment|// Configured fields
DECL|field|maximumRedeliveries
specifier|private
name|Integer
name|maximumRedeliveries
decl_stmt|;
DECL|field|redeliveryDelay
specifier|private
name|Long
name|redeliveryDelay
decl_stmt|;
DECL|field|backOffMultiplier
specifier|private
name|Double
name|backOffMultiplier
decl_stmt|;
DECL|field|maximumRedeliveryDelay
specifier|private
name|Long
name|maximumRedeliveryDelay
decl_stmt|;
DECL|field|camelRouteId
specifier|private
name|String
name|camelRouteId
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|checkProperties
argument_list|()
expr_stmt|;
name|errorHandler
argument_list|(
name|defaultErrorHandler
argument_list|()
operator|.
name|maximumRedeliveries
argument_list|(
name|maximumRedeliveries
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
name|redeliveryDelay
argument_list|)
operator|.
name|backOffMultiplier
argument_list|(
name|backOffMultiplier
argument_list|)
operator|.
name|maximumRedeliveryDelay
argument_list|(
name|maximumRedeliveryDelay
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"{{from}}"
argument_list|)
operator|.
name|routeId
argument_list|(
name|camelRouteId
argument_list|)
operator|.
name|log
argument_list|(
literal|"{{messageOk}}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"{{to}}"
argument_list|)
expr_stmt|;
block|}
DECL|method|checkProperties ()
specifier|private
name|void
name|checkProperties
parameter_list|()
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|maximumRedeliveries
argument_list|,
literal|"maximumRedeliveries property is not set"
argument_list|)
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|redeliveryDelay
argument_list|,
literal|"redeliveryDelay property is not set"
argument_list|)
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|backOffMultiplier
argument_list|,
literal|"backOffMultiplier property is not set"
argument_list|)
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|maximumRedeliveryDelay
argument_list|,
literal|"maximumRedeliveryDelay property is not set"
argument_list|)
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|camelRouteId
argument_list|,
literal|"camelRouteId property is not set"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

