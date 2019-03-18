begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.zipkin.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|zipkin
operator|.
name|http
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
name|zipkin
operator|.
name|ZipkinOneRouteFallbackTest
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
name|zipkin
operator|.
name|ZipkinTracer
import|;
end_import

begin_import
import|import
name|zipkin2
operator|.
name|reporter
operator|.
name|AsyncReporter
import|;
end_import

begin_import
import|import
name|zipkin2
operator|.
name|reporter
operator|.
name|urlconnection
operator|.
name|URLConnectionSender
import|;
end_import

begin_comment
comment|/**  * Integration test requires running Zipkin running  *  *<p>The easiest way to run is locally:  *<pre>{@code  * curl -sSL https://zipkin.io/quickstart.sh | bash -s  * java -jar zipkin.jar  * }</pre>  */
end_comment

begin_class
DECL|class|ZipkinOneRouteFallbackHttp
specifier|public
class|class
name|ZipkinOneRouteFallbackHttp
extends|extends
name|ZipkinOneRouteFallbackTest
block|{
DECL|method|setSpanReporter (ZipkinTracer zipkin)
annotation|@
name|Override
specifier|protected
name|void
name|setSpanReporter
parameter_list|(
name|ZipkinTracer
name|zipkin
parameter_list|)
block|{
name|zipkin
operator|.
name|setSpanReporter
argument_list|(
name|AsyncReporter
operator|.
name|create
argument_list|(
name|URLConnectionSender
operator|.
name|create
argument_list|(
literal|"http://locahost:9411/api/v2/spans"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

