begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.geocoder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|geocoder
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
name|Consumer
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
name|Producer
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
name|impl
operator|.
name|DefaultEndpoint
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
import|;
end_import

begin_comment
comment|/**  * Represents a GeoCoder endpoint.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"geocoder"
argument_list|,
name|label
operator|=
literal|"api,location"
argument_list|)
DECL|class|GeoCoderEndpoint
specifier|public
class|class
name|GeoCoderEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
DECL|field|address
specifier|private
name|String
name|address
decl_stmt|;
annotation|@
name|UriPath
DECL|field|latlng
specifier|private
name|String
name|latlng
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"en"
argument_list|)
DECL|field|language
specifier|private
name|String
name|language
init|=
literal|"en"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clientKey
specifier|private
name|String
name|clientKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|headersOnly
specifier|private
name|boolean
name|headersOnly
decl_stmt|;
DECL|method|GeoCoderEndpoint ()
specifier|public
name|GeoCoderEndpoint
parameter_list|()
block|{     }
DECL|method|GeoCoderEndpoint (String uri, GeoCoderComponent component)
specifier|public
name|GeoCoderEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|GeoCoderComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|GeoCoderProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Cannot consume from this component"
argument_list|)
throw|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
name|language
return|;
block|}
DECL|method|setLanguage (String language)
specifier|public
name|void
name|setLanguage
parameter_list|(
name|String
name|language
parameter_list|)
block|{
name|this
operator|.
name|language
operator|=
name|language
expr_stmt|;
block|}
DECL|method|getAddress ()
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
DECL|method|setAddress (String address)
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
DECL|method|getLatlng ()
specifier|public
name|String
name|getLatlng
parameter_list|()
block|{
return|return
name|latlng
return|;
block|}
DECL|method|setLatlng (String latlng)
specifier|public
name|void
name|setLatlng
parameter_list|(
name|String
name|latlng
parameter_list|)
block|{
name|this
operator|.
name|latlng
operator|=
name|latlng
expr_stmt|;
block|}
DECL|method|isHeadersOnly ()
specifier|public
name|boolean
name|isHeadersOnly
parameter_list|()
block|{
return|return
name|headersOnly
return|;
block|}
DECL|method|setHeadersOnly (boolean headersOnly)
specifier|public
name|void
name|setHeadersOnly
parameter_list|(
name|boolean
name|headersOnly
parameter_list|)
block|{
name|this
operator|.
name|headersOnly
operator|=
name|headersOnly
expr_stmt|;
block|}
DECL|method|getClientId ()
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
DECL|method|setClientId (String clientId)
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
block|}
DECL|method|getClientKey ()
specifier|public
name|String
name|getClientKey
parameter_list|()
block|{
return|return
name|clientKey
return|;
block|}
DECL|method|setClientKey (String clientKey)
specifier|public
name|void
name|setClientKey
parameter_list|(
name|String
name|clientKey
parameter_list|)
block|{
name|this
operator|.
name|clientKey
operator|=
name|clientKey
expr_stmt|;
block|}
block|}
end_class

end_unit

