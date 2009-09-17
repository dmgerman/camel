begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.util
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
name|util
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|DestinationFactory
import|;
end_import

begin_class
DECL|class|NullDestinationFactory
specifier|public
class|class
name|NullDestinationFactory
implements|implements
name|DestinationFactory
block|{
DECL|method|getDestination (EndpointInfo ei)
specifier|public
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
throws|throws
name|IOException
block|{
comment|// setup the endpoint information
name|ei
operator|.
name|setAddress
argument_list|(
literal|"local://"
operator|+
name|ei
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"/"
operator|+
name|ei
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
comment|// working as the dispatch mode, the binding factory will not add interceptor
comment|//ei.getBinding().setProperty(AbstractBindingFactory.DATABINDING_DISABLED, Boolean.TRUE);
comment|// do nothing here , just creating a null destination to store the observer
return|return
operator|new
name|NullDestination
argument_list|()
return|;
block|}
DECL|method|getTransportIds ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getTransportIds
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|getUriPrefixes ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getUriPrefixes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

