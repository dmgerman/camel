begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.cxfbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|cxfbean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|component
operator|.
name|cxf
operator|.
name|transport
operator|.
name|CamelTransportFactory
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|EndpointInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|Destination
import|;
end_import

begin_comment
comment|/**  * CXF Bean TransportFactory that overrides CamelTransportFactory to create  * a specific Destination (@link CxfBeanDestination}.  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfBeanTransportFactory
specifier|public
class|class
name|CxfBeanTransportFactory
extends|extends
name|CamelTransportFactory
block|{
DECL|field|TRANSPORT_ID
specifier|public
specifier|static
specifier|final
name|String
name|TRANSPORT_ID
init|=
literal|"http://cxf.apache.org/transports/camel/cxfbean"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CxfBeanTransportFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cxfBeanComponent
specifier|private
name|CxfBeanComponent
name|cxfBeanComponent
decl_stmt|;
annotation|@
name|Override
DECL|method|getDestination (EndpointInfo endpointInfo)
specifier|public
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Create CxfBeanDestination: "
operator|+
name|endpointInfo
argument_list|)
expr_stmt|;
block|}
comment|// lookup endpoint from component instead of CamelContext because it may not
comment|// be added to the CamelContext yet.
return|return
operator|new
name|CxfBeanDestination
argument_list|(
name|cxfBeanComponent
argument_list|,
name|getBus
argument_list|()
argument_list|,
name|this
argument_list|,
name|endpointInfo
argument_list|)
return|;
block|}
DECL|method|setCxfBeanComponent (CxfBeanComponent cxfBeanComponent)
specifier|public
name|void
name|setCxfBeanComponent
parameter_list|(
name|CxfBeanComponent
name|cxfBeanComponent
parameter_list|)
block|{
name|this
operator|.
name|cxfBeanComponent
operator|=
name|cxfBeanComponent
expr_stmt|;
block|}
block|}
end_class

end_unit

