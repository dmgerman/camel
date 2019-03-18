begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|AssumptionViolatedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_class
DECL|class|GeoCoderApiKeyTestBase
specifier|public
class|class
name|GeoCoderApiKeyTestBase
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
block|{
name|String
name|apiKey
init|=
name|getApiKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|apiKey
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|AssumptionViolatedException
argument_list|(
literal|"API key not found in CAMEL_GEOCODER_APIKEY environment variable, skipping this test"
argument_list|)
throw|;
block|}
block|}
DECL|method|getApiKey ()
specifier|protected
name|String
name|getApiKey
parameter_list|()
block|{
return|return
name|System
operator|.
name|getenv
argument_list|(
literal|"CAMEL_GEOCODER_APIKEY"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

