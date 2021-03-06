begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.soap.headers
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
name|soap
operator|.
name|headers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|common
operator|.
name|header
operator|.
name|MessageHeaderFilter
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
name|HeaderFilterStrategy
operator|.
name|Direction
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
name|headers
operator|.
name|Header
import|;
end_import

begin_class
DECL|class|CustomHeaderFilter
specifier|public
class|class
name|CustomHeaderFilter
implements|implements
name|MessageHeaderFilter
block|{
DECL|field|ACTIVATION_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|ACTIVATION_NAMESPACE
init|=
literal|"http://cxf.apache.org/bindings/custom"
decl_stmt|;
DECL|field|ACTIVATION_NAMESPACES
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|ACTIVATION_NAMESPACES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|ACTIVATION_NAMESPACE
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|getActivationNamespaces ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getActivationNamespaces
parameter_list|()
block|{
return|return
name|ACTIVATION_NAMESPACES
return|;
block|}
annotation|@
name|Override
DECL|method|filter (Direction direction, List<Header> headers)
specifier|public
name|void
name|filter
parameter_list|(
name|Direction
name|direction
parameter_list|,
name|List
argument_list|<
name|Header
argument_list|>
name|headers
parameter_list|)
block|{     }
block|}
end_class

end_unit

