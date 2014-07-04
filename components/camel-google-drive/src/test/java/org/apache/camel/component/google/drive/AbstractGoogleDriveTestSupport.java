begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.drive
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|drive
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|HashMap
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
name|Properties
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
name|CamelContext
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
name|CamelExecutionException
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|IntrospectionSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_class
DECL|class|AbstractGoogleDriveTestSupport
specifier|public
specifier|abstract
class|class
name|AbstractGoogleDriveTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|CAMEL_TEST_TAG
specifier|protected
specifier|static
specifier|final
name|String
name|CAMEL_TEST_TAG
init|=
literal|"camel_was_here"
decl_stmt|;
DECL|field|CAMEL_TEST_FILE
specifier|protected
specifier|static
specifier|final
name|String
name|CAMEL_TEST_FILE
init|=
literal|"CamelTestFile"
decl_stmt|;
DECL|field|LINE_SEPARATOR
specifier|private
specifier|static
specifier|final
name|String
name|LINE_SEPARATOR
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
DECL|field|TEST_OPTIONS_PROPERTIES
specifier|private
specifier|static
specifier|final
name|String
name|TEST_OPTIONS_PROPERTIES
init|=
literal|"/test-options.properties"
decl_stmt|;
DECL|field|REFRESH_TOKEN_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|REFRESH_TOKEN_PROPERTY
init|=
literal|"refreshToken"
decl_stmt|;
DECL|field|testUserId
specifier|protected
specifier|static
name|String
name|testUserId
decl_stmt|;
DECL|field|refreshToken
specifier|private
specifier|static
name|String
name|refreshToken
decl_stmt|;
DECL|field|propertyText
specifier|private
specifier|static
name|String
name|propertyText
decl_stmt|;
DECL|field|testFolderId
specifier|protected
specifier|static
name|String
name|testFolderId
decl_stmt|;
DECL|field|testFileId
specifier|protected
specifier|static
name|String
name|testFileId
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|InputStream
name|in
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|TEST_OPTIONS_PROPERTIES
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|TEST_OPTIONS_PROPERTIES
operator|+
literal|" could not be found"
argument_list|)
throw|;
block|}
specifier|final
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|final
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|line
argument_list|)
operator|.
name|append
argument_list|(
name|LINE_SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|propertyText
operator|=
name|builder
operator|.
name|toString
argument_list|()
expr_stmt|;
specifier|final
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|properties
operator|.
name|load
argument_list|(
operator|new
name|StringReader
argument_list|(
name|propertyText
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s could not be loaded: %s"
argument_list|,
name|TEST_OPTIONS_PROPERTIES
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// cache test properties
name|refreshToken
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
name|REFRESH_TOKEN_PROPERTY
argument_list|)
expr_stmt|;
name|testFolderId
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"testFolderId"
argument_list|)
expr_stmt|;
name|testFileId
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"testFileId"
argument_list|)
expr_stmt|;
name|testUserId
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"testUserId"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|options
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|GoogleDriveConfiguration
name|configuration
init|=
operator|new
name|GoogleDriveConfiguration
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|options
argument_list|)
expr_stmt|;
comment|// add GoogleDriveComponent  to Camel context
specifier|final
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|GoogleDriveComponent
name|component
init|=
operator|new
name|GoogleDriveComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"box"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|AfterClass
DECL|method|tearDownAfterClass ()
specifier|public
specifier|static
name|void
name|tearDownAfterClass
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelTestSupport
operator|.
name|tearDownAfterClass
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
comment|// only create the context once for this class
return|return
literal|true
return|;
block|}
DECL|method|requestBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
operator|(
name|T
operator|)
name|template
argument_list|()
operator|.
name|requestBodyAndHeaders
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
return|;
block|}
DECL|method|requestBody (String endpoint, Object body)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBody
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
operator|(
name|T
operator|)
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
name|endpoint
argument_list|,
name|body
argument_list|)
return|;
block|}
block|}
end_class

end_unit

