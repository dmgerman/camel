begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.microprofile.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|microprofile
operator|.
name|health
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|json
operator|.
name|Json
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|json
operator|.
name|JsonObject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|json
operator|.
name|stream
operator|.
name|JsonParser
import|;
end_import

begin_import
import|import
name|io
operator|.
name|smallrye
operator|.
name|health
operator|.
name|SmallRyeHealth
import|;
end_import

begin_import
import|import
name|io
operator|.
name|smallrye
operator|.
name|health
operator|.
name|SmallRyeHealthReporter
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
name|health
operator|.
name|HealthCheck
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
name|health
operator|.
name|HealthCheckResultBuilder
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|health
operator|.
name|HealthCheckResponse
import|;
end_import

begin_class
DECL|class|CamelMicroProfileHealthTestSupport
specifier|public
class|class
name|CamelMicroProfileHealthTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|reporter
specifier|protected
name|SmallRyeHealthReporter
name|reporter
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|reporter
operator|=
operator|new
name|SmallRyeHealthReporter
argument_list|()
expr_stmt|;
block|}
DECL|method|assertHealthCheckOutput (String expectedName, HealthCheckResponse.State expectedState, JsonObject healthObject, Consumer<JsonObject> dataObjectAssertions)
specifier|protected
name|void
name|assertHealthCheckOutput
parameter_list|(
name|String
name|expectedName
parameter_list|,
name|HealthCheckResponse
operator|.
name|State
name|expectedState
parameter_list|,
name|JsonObject
name|healthObject
parameter_list|,
name|Consumer
argument_list|<
name|JsonObject
argument_list|>
name|dataObjectAssertions
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expectedName
argument_list|,
name|healthObject
operator|.
name|getString
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedState
operator|.
name|name
argument_list|()
argument_list|,
name|healthObject
operator|.
name|getString
argument_list|(
literal|"status"
argument_list|)
argument_list|)
expr_stmt|;
name|dataObjectAssertions
operator|.
name|accept
argument_list|(
name|healthObject
operator|.
name|getJsonObject
argument_list|(
literal|"data"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getHealthJson (SmallRyeHealth health)
specifier|protected
name|JsonObject
name|getHealthJson
parameter_list|(
name|SmallRyeHealth
name|health
parameter_list|)
block|{
name|JsonParser
name|parser
init|=
name|Json
operator|.
name|createParser
argument_list|(
operator|new
name|StringReader
argument_list|(
name|getHealthOutput
argument_list|(
name|health
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Health check content is empty"
argument_list|,
name|parser
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|parser
operator|.
name|next
argument_list|()
expr_stmt|;
return|return
name|parser
operator|.
name|getObject
argument_list|()
return|;
block|}
DECL|method|getHealthOutput (SmallRyeHealth health)
specifier|protected
name|String
name|getHealthOutput
parameter_list|(
name|SmallRyeHealth
name|health
parameter_list|)
block|{
name|ByteArrayOutputStream
name|outputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|reporter
operator|.
name|reportHealth
argument_list|(
name|outputStream
argument_list|,
name|health
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
argument_list|(
name|outputStream
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
DECL|method|createLivenessCheck (String id, boolean enabled, Consumer<HealthCheckResultBuilder> consumer)
specifier|protected
name|HealthCheck
name|createLivenessCheck
parameter_list|(
name|String
name|id
parameter_list|,
name|boolean
name|enabled
parameter_list|,
name|Consumer
argument_list|<
name|HealthCheckResultBuilder
argument_list|>
name|consumer
parameter_list|)
block|{
name|HealthCheck
name|healthCheck
init|=
operator|new
name|AbstractCamelMicroProfileLivenessCheck
argument_list|(
name|id
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|doCall
parameter_list|(
name|HealthCheckResultBuilder
name|builder
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
name|consumer
operator|.
name|accept
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|healthCheck
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
return|return
name|healthCheck
return|;
block|}
DECL|method|createReadinessCheck (String id, boolean enabled, Consumer<HealthCheckResultBuilder> consumer)
specifier|protected
name|HealthCheck
name|createReadinessCheck
parameter_list|(
name|String
name|id
parameter_list|,
name|boolean
name|enabled
parameter_list|,
name|Consumer
argument_list|<
name|HealthCheckResultBuilder
argument_list|>
name|consumer
parameter_list|)
block|{
name|HealthCheck
name|healthCheck
init|=
operator|new
name|AbstractCamelMicroProfileReadinessCheck
argument_list|(
name|id
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|doCall
parameter_list|(
name|HealthCheckResultBuilder
name|builder
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
name|consumer
operator|.
name|accept
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|healthCheck
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
return|return
name|healthCheck
return|;
block|}
block|}
end_class

end_unit

