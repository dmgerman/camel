begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.springboot.geocoder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|springboot
operator|.
name|geocoder
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|maps
operator|.
name|model
operator|.
name|GeocodingResult
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
operator|.
name|RestParamType
operator|.
name|query
import|;
end_import

begin_comment
comment|/**  * A simple Camel REST DSL route example using the Geocoder component and documented with Swagger  */
end_comment

begin_class
annotation|@
name|Component
DECL|class|CamelGeocoderRoute
specifier|public
class|class
name|CamelGeocoderRoute
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// rest-dsl is also configured in the application.properties file
name|rest
argument_list|(
literal|"/geocoder"
argument_list|)
operator|.
name|description
argument_list|(
literal|"Geocoder REST service"
argument_list|)
operator|.
name|consumes
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|produces
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|description
argument_list|(
literal|"Geocoder address lookup"
argument_list|)
operator|.
name|outType
argument_list|(
name|GeocodingResult
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|param
argument_list|()
operator|.
name|name
argument_list|(
literal|"address"
argument_list|)
operator|.
name|type
argument_list|(
name|query
argument_list|)
operator|.
name|description
argument_list|(
literal|"The address to lookup"
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"string"
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|responseMessage
argument_list|()
operator|.
name|code
argument_list|(
literal|200
argument_list|)
operator|.
name|message
argument_list|(
literal|"Geocoder successful"
argument_list|)
operator|.
name|endResponseMessage
argument_list|()
comment|// call the geocoder to lookup details from the provided address
operator|.
name|toD
argument_list|(
literal|"geocoder:address:${header.address})"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

