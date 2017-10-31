begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yql
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
name|Exchange
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
name|yql
operator|.
name|client
operator|.
name|YqlClient
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
name|yql
operator|.
name|client
operator|.
name|YqlResponse
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
name|yql
operator|.
name|configuration
operator|.
name|YqlConfiguration
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
name|DefaultProducer
import|;
end_import

begin_comment
comment|/**  * A Producer that send messages to YQL  */
end_comment

begin_class
DECL|class|YqlProducer
specifier|public
class|class
name|YqlProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|CAMEL_YQL_HTTP_STATUS
specifier|static
specifier|final
name|String
name|CAMEL_YQL_HTTP_STATUS
init|=
literal|"CamelYqlHttpStatus"
decl_stmt|;
DECL|field|CAMEL_YQL_HTTP_REQUEST
specifier|static
specifier|final
name|String
name|CAMEL_YQL_HTTP_REQUEST
init|=
literal|"CamelYqlHttpRequest"
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|YqlEndpoint
name|endpoint
decl_stmt|;
DECL|field|yqlClient
specifier|private
specifier|final
name|YqlClient
name|yqlClient
decl_stmt|;
DECL|method|YqlProducer (final YqlEndpoint endpoint)
name|YqlProducer
parameter_list|(
specifier|final
name|YqlEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|yqlClient
operator|=
operator|new
name|YqlClient
argument_list|(
name|endpoint
operator|.
name|getHttpClient
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|YqlConfiguration
name|configuration
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
specifier|final
name|YqlResponse
name|yqlResponse
init|=
name|yqlClient
operator|.
name|get
argument_list|(
name|configuration
operator|.
name|getQuery
argument_list|()
argument_list|,
name|configuration
operator|.
name|getFormat
argument_list|()
argument_list|,
name|configuration
operator|.
name|isDiagnostics
argument_list|()
argument_list|,
name|configuration
operator|.
name|getCallback
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CAMEL_YQL_HTTP_STATUS
argument_list|,
name|yqlResponse
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CAMEL_YQL_HTTP_REQUEST
argument_list|,
name|yqlResponse
operator|.
name|getHttpRequest
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|yqlResponse
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

