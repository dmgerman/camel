begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.avro
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|avro
package|;
end_package

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
name|FileInputStream
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
name|test
operator|.
name|AvailablePortFinder
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
name|CamelTestSupport
import|;
end_import

begin_class
DECL|class|AvroTestSupport
specifier|public
class|class
name|AvroTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|method|setupFreePort (String name)
specifier|public
specifier|static
name|int
name|setupFreePort
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|int
name|port
init|=
operator|-
literal|1
decl_stmt|;
name|FileInputStream
name|fis
init|=
literal|null
decl_stmt|;
name|FileOutputStream
name|fos
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|File
name|propertiesFile
init|=
operator|new
name|File
argument_list|(
literal|"target/custom.properties"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|propertiesFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|propertiesFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
block|}
name|fis
operator|=
operator|new
name|FileInputStream
argument_list|(
name|propertiesFile
argument_list|)
expr_stmt|;
name|fos
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|propertiesFile
argument_list|)
expr_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|fis
argument_list|)
expr_stmt|;
if|if
condition|(
name|properties
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
comment|// find a free port number from 9100 onwards, and write that in the custom.properties file
comment|// which we will use for the unit tests, to avoid port number in use problems
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|9100
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|store
argument_list|(
name|fos
argument_list|,
literal|"avro"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//Ignore
block|}
finally|finally
block|{
if|if
condition|(
name|fis
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{                 }
block|}
if|if
condition|(
name|fos
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{                 }
block|}
block|}
return|return
name|port
return|;
block|}
block|}
end_class

end_unit

