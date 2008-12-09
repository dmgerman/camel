begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|Processor
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
name|processor
operator|.
name|RoutingSlip
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
name|spi
operator|.
name|RouteContext
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;routingSlip/&gt; element  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"routingSlip"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RoutingSlipType
specifier|public
class|class
name|RoutingSlipType
extends|extends
name|ProcessorType
argument_list|<
name|ProcessorType
argument_list|>
block|{
DECL|field|ROUTING_SLIP_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|ROUTING_SLIP_HEADER
init|=
literal|"routingSlipHeader"
decl_stmt|;
DECL|field|DEFAULT_DELIMITER
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_DELIMITER
init|=
literal|","
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|uriDelimiter
specifier|private
name|String
name|uriDelimiter
decl_stmt|;
DECL|method|RoutingSlipType ()
specifier|public
name|RoutingSlipType
parameter_list|()
block|{
name|this
argument_list|(
name|ROUTING_SLIP_HEADER
argument_list|,
name|DEFAULT_DELIMITER
argument_list|)
expr_stmt|;
block|}
DECL|method|RoutingSlipType (String headerName)
specifier|public
name|RoutingSlipType
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|this
argument_list|(
name|headerName
argument_list|,
name|DEFAULT_DELIMITER
argument_list|)
expr_stmt|;
block|}
DECL|method|RoutingSlipType (String headerName, String uriDelimiter)
specifier|public
name|RoutingSlipType
parameter_list|(
name|String
name|headerName
parameter_list|,
name|String
name|uriDelimiter
parameter_list|)
block|{
name|setHeaderName
argument_list|(
name|headerName
argument_list|)
expr_stmt|;
name|setUriDelimiter
argument_list|(
name|uriDelimiter
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
literal|"RoutingSlip[headerName="
operator|+
name|getHeaderName
argument_list|()
operator|+
literal|", uriDelimiter="
operator|+
name|getUriDelimiter
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"routingSlip"
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getHeaderName
argument_list|()
argument_list|,
literal|"headerName"
argument_list|)
expr_stmt|;
return|return
operator|new
name|RoutingSlip
argument_list|(
name|getHeaderName
argument_list|()
argument_list|,
name|getUriDelimiter
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
DECL|method|setHeaderName (String headerName)
specifier|public
name|void
name|setHeaderName
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|this
operator|.
name|headerName
operator|=
name|headerName
expr_stmt|;
block|}
DECL|method|getHeaderName ()
specifier|public
name|String
name|getHeaderName
parameter_list|()
block|{
return|return
name|this
operator|.
name|headerName
return|;
block|}
DECL|method|setUriDelimiter (String uriDelimiter)
specifier|public
name|void
name|setUriDelimiter
parameter_list|(
name|String
name|uriDelimiter
parameter_list|)
block|{
name|this
operator|.
name|uriDelimiter
operator|=
name|uriDelimiter
expr_stmt|;
block|}
DECL|method|getUriDelimiter ()
specifier|public
name|String
name|getUriDelimiter
parameter_list|()
block|{
return|return
name|uriDelimiter
return|;
block|}
block|}
end_class

end_unit

