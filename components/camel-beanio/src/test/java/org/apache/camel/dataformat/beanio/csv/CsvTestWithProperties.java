begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.beanio.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|beanio
operator|.
name|csv
package|;
end_package

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
name|builder
operator|.
name|RouteBuilder
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
name|dataformat
operator|.
name|beanio
operator|.
name|BeanIODataFormat
import|;
end_import

begin_class
DECL|class|CsvTestWithProperties
specifier|public
class|class
name|CsvTestWithProperties
extends|extends
name|CsvTest
block|{
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e1
comment|// setup beanio data format using the mapping file, loaded from the classpath
name|BeanIODataFormat
name|format
init|=
operator|new
name|BeanIODataFormat
argument_list|(
literal|"org/apache/camel/dataformat/beanio/csv/mappingsWithProperties.xml"
argument_list|,
literal|"stream1"
argument_list|)
decl_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"field1"
argument_list|,
literal|"firstName"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"field2"
argument_list|,
literal|"lastName"
argument_list|)
expr_stmt|;
name|format
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
comment|// a route which uses the bean io data format to format a CSV data
comment|// to java objects
name|from
argument_list|(
literal|"direct:unmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|format
argument_list|)
comment|// and then split the message body so we get a message for each row
operator|.
name|split
argument_list|(
name|body
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:beanio-unmarshal"
argument_list|)
expr_stmt|;
comment|// convert list of java objects back to flat format
name|from
argument_list|(
literal|"direct:marshal"
argument_list|)
operator|.
name|marshal
argument_list|(
name|format
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:beanio-marshal"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

