begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jhc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jhc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Endpoint
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
name|HeaderFilterStrategyAware
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
name|DefaultComponent
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
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|BasicHttpParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|HttpConnectionParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|HttpParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|HttpProtocolParams
import|;
end_import

begin_class
DECL|class|JhcComponent
specifier|public
class|class
name|JhcComponent
extends|extends
name|DefaultComponent
implements|implements
name|HeaderFilterStrategyAware
block|{
DECL|field|params
specifier|private
name|HttpParams
name|params
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|method|JhcComponent ()
specifier|public
name|JhcComponent
parameter_list|()
block|{
name|setHeaderFilterStrategy
argument_list|(
operator|new
name|JhcHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|params
operator|=
operator|new
name|BasicHttpParams
argument_list|()
operator|.
name|setIntParameter
argument_list|(
name|HttpConnectionParams
operator|.
name|SO_TIMEOUT
argument_list|,
literal|5000
argument_list|)
operator|.
name|setIntParameter
argument_list|(
name|HttpConnectionParams
operator|.
name|CONNECTION_TIMEOUT
argument_list|,
literal|10000
argument_list|)
operator|.
name|setIntParameter
argument_list|(
name|HttpConnectionParams
operator|.
name|SOCKET_BUFFER_SIZE
argument_list|,
literal|8
operator|*
literal|1024
argument_list|)
operator|.
name|setBooleanParameter
argument_list|(
name|HttpConnectionParams
operator|.
name|STALE_CONNECTION_CHECK
argument_list|,
literal|false
argument_list|)
operator|.
name|setBooleanParameter
argument_list|(
name|HttpConnectionParams
operator|.
name|TCP_NODELAY
argument_list|,
literal|true
argument_list|)
operator|.
name|setParameter
argument_list|(
name|HttpProtocolParams
operator|.
name|USER_AGENT
argument_list|,
literal|"Camel-JhcComponent/1.1"
argument_list|)
expr_stmt|;
block|}
DECL|method|getParams ()
specifier|public
name|HttpParams
name|getParams
parameter_list|()
block|{
return|return
name|params
return|;
block|}
DECL|method|setParams (HttpParams params)
specifier|public
name|void
name|setParams
parameter_list|(
name|HttpParams
name|params
parameter_list|)
block|{
name|this
operator|.
name|params
operator|=
name|params
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|JhcEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
operator|new
name|URI
argument_list|(
name|uri
operator|.
name|substring
argument_list|(
name|uri
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|+
literal|1
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy strategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|)
block|{
name|headerFilterStrategy
operator|=
name|strategy
expr_stmt|;
block|}
block|}
end_class

end_unit

