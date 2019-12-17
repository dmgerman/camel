begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.openapi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|openapi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_import
import|import
name|io
operator|.
name|apicurio
operator|.
name|datamodels
operator|.
name|core
operator|.
name|models
operator|.
name|common
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|io
operator|.
name|apicurio
operator|.
name|datamodels
operator|.
name|openapi
operator|.
name|v2
operator|.
name|models
operator|.
name|Oas20Document
import|;
end_import

begin_import
import|import
name|io
operator|.
name|apicurio
operator|.
name|datamodels
operator|.
name|openapi
operator|.
name|v3
operator|.
name|models
operator|.
name|Oas30Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|params
operator|.
name|ParameterizedTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|params
operator|.
name|provider
operator|.
name|Arguments
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|params
operator|.
name|provider
operator|.
name|MethodSource
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|params
operator|.
name|provider
operator|.
name|Arguments
operator|.
name|arguments
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|spy
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verifyZeroInteractions
import|;
end_import

begin_class
DECL|class|RestOpenApiSupportTest
specifier|public
class|class
name|RestOpenApiSupportTest
block|{
annotation|@
name|Test
DECL|method|shouldAdaptFromXForwardHeaders ()
specifier|public
name|void
name|shouldAdaptFromXForwardHeaders
parameter_list|()
block|{
name|Oas20Document
name|doc
init|=
operator|new
name|Oas20Document
argument_list|()
decl_stmt|;
name|doc
operator|.
name|basePath
operator|=
literal|"/base"
expr_stmt|;
specifier|final
name|Oas20Document
name|openApi
init|=
name|spy
argument_list|(
name|doc
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RestOpenApiSupport
operator|.
name|HEADER_X_FORWARDED_PREFIX
argument_list|,
literal|"/prefix"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RestOpenApiSupport
operator|.
name|HEADER_X_FORWARDED_HOST
argument_list|,
literal|"host"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RestOpenApiSupport
operator|.
name|HEADER_X_FORWARDED_PROTO
argument_list|,
literal|"http, HTTPS "
argument_list|)
expr_stmt|;
name|RestOpenApiSupport
operator|.
name|setupXForwardedHeaders
argument_list|(
name|openApi
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|openApi
operator|.
name|basePath
argument_list|,
literal|"/prefix/base"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|openApi
operator|.
name|host
argument_list|,
literal|"host"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|openApi
operator|.
name|schemes
operator|.
name|contains
argument_list|(
literal|"http"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|openApi
operator|.
name|schemes
operator|.
name|contains
argument_list|(
literal|"https"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldAdaptFromXForwardHeadersV3 ()
specifier|public
name|void
name|shouldAdaptFromXForwardHeadersV3
parameter_list|()
block|{
name|Oas30Document
name|doc
init|=
operator|new
name|Oas30Document
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addServer
argument_list|(
literal|"http://myhost/base"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
specifier|final
name|Oas30Document
name|openApi
init|=
name|spy
argument_list|(
name|doc
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RestOpenApiSupport
operator|.
name|HEADER_X_FORWARDED_PREFIX
argument_list|,
literal|"/prefix"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RestOpenApiSupport
operator|.
name|HEADER_X_FORWARDED_HOST
argument_list|,
literal|"host"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RestOpenApiSupport
operator|.
name|HEADER_X_FORWARDED_PROTO
argument_list|,
literal|"http, HTTPS "
argument_list|)
expr_stmt|;
name|RestOpenApiSupport
operator|.
name|setupXForwardedHeaders
argument_list|(
name|openApi
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|openApi
operator|.
name|getServers
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|url
argument_list|,
literal|"http://host/prefix/base"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|openApi
operator|.
name|getServers
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|url
argument_list|,
literal|"https://host/prefix/base"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ParameterizedTest
annotation|@
name|MethodSource
argument_list|(
literal|"basePathAndPrefixVariations"
argument_list|)
DECL|method|shouldAdaptWithVaryingBasePathsAndPrefixes (final String prefix, final String basePath, final String expected)
specifier|public
name|void
name|shouldAdaptWithVaryingBasePathsAndPrefixes
parameter_list|(
specifier|final
name|String
name|prefix
parameter_list|,
specifier|final
name|String
name|basePath
parameter_list|,
specifier|final
name|String
name|expected
parameter_list|)
block|{
name|Oas20Document
name|doc
init|=
operator|new
name|Oas20Document
argument_list|()
decl_stmt|;
name|doc
operator|.
name|basePath
operator|=
name|basePath
expr_stmt|;
specifier|final
name|Oas20Document
name|openApi
init|=
name|spy
argument_list|(
name|doc
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RestOpenApiSupport
operator|.
name|HEADER_X_FORWARDED_PREFIX
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
name|RestOpenApiSupport
operator|.
name|setupXForwardedHeaders
argument_list|(
name|openApi
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|openApi
operator|.
name|basePath
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ParameterizedTest
annotation|@
name|MethodSource
argument_list|(
literal|"basePathAndPrefixVariations"
argument_list|)
DECL|method|shouldAdaptWithVaryingBasePathsAndPrefixesV3 (final String prefix, final String basePath, final String expected)
specifier|public
name|void
name|shouldAdaptWithVaryingBasePathsAndPrefixesV3
parameter_list|(
specifier|final
name|String
name|prefix
parameter_list|,
specifier|final
name|String
name|basePath
parameter_list|,
specifier|final
name|String
name|expected
parameter_list|)
block|{
name|Oas30Document
name|doc
init|=
operator|new
name|Oas30Document
argument_list|()
decl_stmt|;
if|if
condition|(
name|basePath
operator|!=
literal|null
condition|)
block|{
name|doc
operator|.
name|addServer
argument_list|(
literal|"http://myhost/"
operator|+
name|basePath
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doc
operator|.
name|addServer
argument_list|(
literal|"http://myhost/"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Oas30Document
name|openApi
init|=
name|spy
argument_list|(
name|doc
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RestOpenApiSupport
operator|.
name|HEADER_X_FORWARDED_PREFIX
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
name|RestOpenApiSupport
operator|.
name|setupXForwardedHeaders
argument_list|(
name|openApi
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|openApi
operator|.
name|getServers
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|url
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ParameterizedTest
annotation|@
name|MethodSource
argument_list|(
literal|"schemeVariations"
argument_list|)
DECL|method|shouldAdaptWithVaryingSchemes (final String xForwardedScheme, final String[] expected)
specifier|public
name|void
name|shouldAdaptWithVaryingSchemes
parameter_list|(
specifier|final
name|String
name|xForwardedScheme
parameter_list|,
specifier|final
name|String
index|[]
name|expected
parameter_list|)
block|{
specifier|final
name|Oas20Document
name|openApi
init|=
name|spy
argument_list|(
operator|new
name|Oas20Document
argument_list|()
argument_list|)
decl_stmt|;
name|RestOpenApiSupport
operator|.
name|setupXForwardedHeaders
argument_list|(
name|openApi
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
name|RestOpenApiSupport
operator|.
name|HEADER_X_FORWARDED_PROTO
argument_list|,
name|xForwardedScheme
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|String
name|scheme
range|:
name|expected
control|)
block|{
name|assertTrue
argument_list|(
name|openApi
operator|.
name|schemes
operator|.
name|contains
argument_list|(
name|scheme
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|ParameterizedTest
annotation|@
name|MethodSource
argument_list|(
literal|"schemeVariations"
argument_list|)
DECL|method|shouldAdaptWithVaryingSchemesV3 (final String xForwardedScheme, final String[] expected)
specifier|public
name|void
name|shouldAdaptWithVaryingSchemesV3
parameter_list|(
specifier|final
name|String
name|xForwardedScheme
parameter_list|,
specifier|final
name|String
index|[]
name|expected
parameter_list|)
block|{
specifier|final
name|Oas30Document
name|openApi
init|=
name|spy
argument_list|(
operator|new
name|Oas30Document
argument_list|()
argument_list|)
decl_stmt|;
name|RestOpenApiSupport
operator|.
name|setupXForwardedHeaders
argument_list|(
name|openApi
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
name|RestOpenApiSupport
operator|.
name|HEADER_X_FORWARDED_PROTO
argument_list|,
name|xForwardedScheme
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|schemas
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|openApi
operator|.
name|servers
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Server
name|server
range|:
name|openApi
operator|.
name|servers
control|)
block|{
try|try
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|server
operator|.
name|url
argument_list|)
decl_stmt|;
name|schemas
operator|.
name|add
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{                                       }
block|}
block|}
for|for
control|(
specifier|final
name|String
name|scheme
range|:
name|expected
control|)
block|{
name|assertTrue
argument_list|(
name|schemas
operator|.
name|contains
argument_list|(
name|scheme
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|shouldNotAdaptFromXForwardHeadersWhenNoHeadersSpecified ()
specifier|public
name|void
name|shouldNotAdaptFromXForwardHeadersWhenNoHeadersSpecified
parameter_list|()
block|{
specifier|final
name|Oas20Document
name|openApi
init|=
name|spy
argument_list|(
operator|new
name|Oas20Document
argument_list|()
argument_list|)
decl_stmt|;
name|RestOpenApiSupport
operator|.
name|setupXForwardedHeaders
argument_list|(
name|openApi
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|verifyZeroInteractions
argument_list|(
name|openApi
argument_list|)
expr_stmt|;
block|}
DECL|method|basePathAndPrefixVariations ()
specifier|static
name|Stream
argument_list|<
name|Arguments
argument_list|>
name|basePathAndPrefixVariations
parameter_list|()
block|{
return|return
name|Stream
operator|.
name|of
argument_list|(
comment|//
name|arguments
argument_list|(
literal|"/prefix"
argument_list|,
literal|"/base"
argument_list|,
literal|"/prefix/base"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"/prefix"
argument_list|,
literal|"/base/"
argument_list|,
literal|"/prefix/base/"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"/prefix"
argument_list|,
literal|"base"
argument_list|,
literal|"/prefix/base"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"/prefix"
argument_list|,
literal|"base/"
argument_list|,
literal|"/prefix/base/"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"/prefix"
argument_list|,
literal|""
argument_list|,
literal|"/prefix"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"/prefix"
argument_list|,
literal|null
argument_list|,
literal|"/prefix"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"/prefix/"
argument_list|,
literal|"/base"
argument_list|,
literal|"/prefix/base"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"/prefix/"
argument_list|,
literal|"/base/"
argument_list|,
literal|"/prefix/base/"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"/prefix/"
argument_list|,
literal|"base"
argument_list|,
literal|"/prefix/base"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"/prefix/"
argument_list|,
literal|"base/"
argument_list|,
literal|"/prefix/base/"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"/prefix/"
argument_list|,
literal|""
argument_list|,
literal|"/prefix/"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"/prefix/"
argument_list|,
literal|null
argument_list|,
literal|"/prefix/"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"prefix"
argument_list|,
literal|"/base"
argument_list|,
literal|"prefix/base"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"prefix"
argument_list|,
literal|"/base/"
argument_list|,
literal|"prefix/base/"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"prefix"
argument_list|,
literal|"base"
argument_list|,
literal|"prefix/base"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"prefix"
argument_list|,
literal|"base/"
argument_list|,
literal|"prefix/base/"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"prefix"
argument_list|,
literal|""
argument_list|,
literal|"prefix"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"prefix"
argument_list|,
literal|null
argument_list|,
literal|"prefix"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"prefix/"
argument_list|,
literal|"/base"
argument_list|,
literal|"prefix/base"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"prefix/"
argument_list|,
literal|"/base/"
argument_list|,
literal|"prefix/base/"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"prefix/"
argument_list|,
literal|"base"
argument_list|,
literal|"prefix/base"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"prefix/"
argument_list|,
literal|"base/"
argument_list|,
literal|"prefix/base/"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"prefix/"
argument_list|,
literal|""
argument_list|,
literal|"prefix/"
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"prefix/"
argument_list|,
literal|null
argument_list|,
literal|"prefix/"
argument_list|)
comment|//
argument_list|)
return|;
block|}
DECL|method|schemeVariations ()
specifier|static
name|Stream
argument_list|<
name|Arguments
argument_list|>
name|schemeVariations
parameter_list|()
block|{
specifier|final
name|String
index|[]
name|none
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
return|return
name|Stream
operator|.
name|of
argument_list|(
comment|//
name|arguments
argument_list|(
literal|null
argument_list|,
name|none
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|""
argument_list|,
name|none
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|","
argument_list|,
name|none
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|" , "
argument_list|,
name|none
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"HTTPS,http"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"https"
block|,
literal|"http"
block|}
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|" HTTPS,  http "
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"https"
block|,
literal|"http"
block|}
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|",http,"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"http"
block|}
argument_list|)
argument_list|,
comment|//
name|arguments
argument_list|(
literal|"hTtpS"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"https"
block|}
argument_list|)
comment|//
argument_list|)
return|;
block|}
block|}
end_class

end_unit

