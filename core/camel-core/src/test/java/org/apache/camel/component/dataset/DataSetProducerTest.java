begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dataset
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dataset
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|ContextTestSupport
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
name|NoSuchHeaderException
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|DataSetProducerTest
specifier|public
class|class
name|DataSetProducerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|dataSet
specifier|protected
name|SimpleDataSet
name|dataSet
init|=
operator|new
name|SimpleDataSet
argument_list|(
literal|20
argument_list|)
decl_stmt|;
DECL|field|dataSetName
specifier|final
name|String
name|dataSetName
init|=
literal|"foo"
decl_stmt|;
DECL|field|dataSetUri
specifier|final
name|String
name|dataSetUri
init|=
literal|"dataset://"
operator|+
name|dataSetName
decl_stmt|;
DECL|field|dataSetUriWithDataSetIndexSetToOff
specifier|final
name|String
name|dataSetUriWithDataSetIndexSetToOff
init|=
name|dataSetUri
operator|+
literal|"?dataSetIndex=off"
decl_stmt|;
DECL|field|dataSetUriWithDataSetIndexSetToLenient
specifier|final
name|String
name|dataSetUriWithDataSetIndexSetToLenient
init|=
name|dataSetUri
operator|+
literal|"?dataSetIndex=lenient"
decl_stmt|;
DECL|field|dataSetUriWithDataSetIndexSetToStrict
specifier|final
name|String
name|dataSetUriWithDataSetIndexSetToStrict
init|=
name|dataSetUri
operator|+
literal|"?dataSetIndex=strict"
decl_stmt|;
DECL|field|sourceUri
specifier|final
name|String
name|sourceUri
init|=
literal|"direct://source"
decl_stmt|;
DECL|field|resultUri
specifier|final
name|String
name|resultUri
init|=
literal|"mock://result"
decl_stmt|;
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|super
operator|.
name|createJndiContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
name|dataSetName
argument_list|,
name|dataSet
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testSendingMessagesExplicitlyToDataSetEndpointWithDataSetIndexHeader ()
specifier|public
name|void
name|testSendingMessagesExplicitlyToDataSetEndpointWithDataSetIndexHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|dataSetUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendingMessagesExplicitlyToDataSetEndpointWithoutDataSetIndexHeader ()
specifier|public
name|void
name|testSendingMessagesExplicitlyToDataSetEndpointWithoutDataSetIndexHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|dataSetUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendingMessagesExplicitlyToDataSetEndpointWithoutDataSetIndexAndDataSetIndexUriParameterSetToOff ()
specifier|public
name|void
name|testSendingMessagesExplicitlyToDataSetEndpointWithoutDataSetIndexAndDataSetIndexUriParameterSetToOff
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
literal|0
operator|==
name|i
operator|%
literal|2
condition|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|dataSetUriWithDataSetIndexSetToLenient
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|dataSetUriWithDataSetIndexSetToLenient
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendingMessagesExplicitlyToDataSetEndpointWithoutDataSetIndexAndDataSetIndexUriParameterSetToLenient ()
specifier|public
name|void
name|testSendingMessagesExplicitlyToDataSetEndpointWithoutDataSetIndexAndDataSetIndexUriParameterSetToLenient
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
literal|0
operator|==
name|i
operator|%
literal|2
condition|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|dataSetUriWithDataSetIndexSetToLenient
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|dataSetUriWithDataSetIndexSetToLenient
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendingMessagesExplicitlyToDataSetEndpointWithoutDataSetIndexAndDataSetIndexUriParameterSetToStrict ()
specifier|public
name|void
name|testSendingMessagesExplicitlyToDataSetEndpointWithoutDataSetIndexAndDataSetIndexUriParameterSetToStrict
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|dataSetUriWithDataSetIndexSetToStrict
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Verify that the CamelDataSetIndex header is optional when the dataSetIndex parameter is unset      */
annotation|@
name|Test
DECL|method|testNotSettingDataSetIndexHeaderWhenDataSetIndexUriParameterIsUnset ()
specifier|public
name|void
name|testNotSettingDataSetIndexHeaderWhenDataSetIndexUriParameterIsUnset
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
literal|0
operator|==
operator|(
name|size
operator|%
literal|2
operator|)
condition|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|dataSetUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|dataSetUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Verify that the CamelDataSetIndex header is ignored when the dataSetIndex URI paramter is set to off      */
annotation|@
name|Test
DECL|method|testNotSettingDataSetIndexHeaderWhenDataSetIndexUriParameterSetToOff ()
specifier|public
name|void
name|testNotSettingDataSetIndexHeaderWhenDataSetIndexUriParameterSetToOff
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
literal|0
operator|==
operator|(
name|size
operator|%
literal|2
operator|)
condition|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|dataSetUriWithDataSetIndexSetToOff
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|size
operator|-
name|i
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|dataSetUriWithDataSetIndexSetToOff
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Verify that the CamelDataSetIndex header is optional when the dataSetIndex URI parameter is set to lenient      */
annotation|@
name|Test
DECL|method|testNotSettingDataSetIndexHeaderWhenDataSetIndexUriParameterSetToLenient ()
specifier|public
name|void
name|testNotSettingDataSetIndexHeaderWhenDataSetIndexUriParameterSetToLenient
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
literal|0
operator|==
operator|(
name|size
operator|%
literal|2
operator|)
condition|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|dataSetUriWithDataSetIndexSetToLenient
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|dataSetUriWithDataSetIndexSetToLenient
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Verify that the CamelDataSetIndex header is required when the dataSetIndex URI parameter is set to strict      */
annotation|@
name|Test
DECL|method|testNotSettingDataSetIndexHeaderWhenDataSetIndexUriParameterSetToStrict ()
specifier|public
name|void
name|testNotSettingDataSetIndexHeaderWhenDataSetIndexUriParameterSetToStrict
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|dataSetUriWithDataSetIndexSetToStrict
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionError
name|assertionError
parameter_list|)
block|{
comment|// Check as much of the string as possible - but the ExchangeID at the end will be unique
name|String
name|expectedErrorString
init|=
name|dataSetUriWithDataSetIndexSetToStrict
operator|+
literal|" Failed due to caught exception: "
operator|+
name|NoSuchHeaderException
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|": No '"
operator|+
name|Exchange
operator|.
name|DATASET_INDEX
operator|+
literal|"' header available of type: java.lang.Long. Exchange"
decl_stmt|;
name|String
name|actualErrorString
init|=
name|assertionError
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|actualErrorString
operator|.
name|startsWith
argument_list|(
name|expectedErrorString
argument_list|)
condition|)
block|{
comment|// This is what we expect
return|return;
block|}
else|else
block|{
throw|throw
name|assertionError
throw|;
block|}
block|}
name|fail
argument_list|(
literal|"AssertionError should have been generated"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataSetIndexUriParameterUnset ()
specifier|public
name|void
name|testDataSetIndexUriParameterUnset
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
name|sourceUri
argument_list|)
operator|.
name|to
argument_list|(
name|dataSetUri
argument_list|)
operator|.
name|to
argument_list|(
name|resultUri
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|resultUri
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
operator|(
name|int
operator|)
name|size
argument_list|)
expr_stmt|;
name|result
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|result
operator|.
name|expectsAscending
argument_list|(
name|header
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|)
operator|.
name|convertTo
argument_list|(
name|Number
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|sourceUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|result
operator|.
name|assertMessagesAscending
argument_list|(
name|header
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|)
operator|.
name|convertTo
argument_list|(
name|Number
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataSetIndexUriParameterSetToOff ()
specifier|public
name|void
name|testDataSetIndexUriParameterSetToOff
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
name|sourceUri
argument_list|)
operator|.
name|to
argument_list|(
name|dataSetUriWithDataSetIndexSetToOff
argument_list|)
operator|.
name|to
argument_list|(
name|resultUri
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|resultUri
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
operator|(
name|int
operator|)
name|size
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectsAscending
argument_list|(
name|header
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|)
operator|.
name|convertTo
argument_list|(
name|Number
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sourceUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataSetIndexUriParameterSetToLenient ()
specifier|public
name|void
name|testDataSetIndexUriParameterSetToLenient
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
name|sourceUri
argument_list|)
operator|.
name|to
argument_list|(
name|dataSetUriWithDataSetIndexSetToLenient
argument_list|)
operator|.
name|to
argument_list|(
name|resultUri
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|resultUri
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
operator|(
name|int
operator|)
name|size
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectsAscending
argument_list|(
name|header
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|)
operator|.
name|convertTo
argument_list|(
name|Number
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sourceUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataSetIndexUriParameterSetToStrict ()
specifier|public
name|void
name|testDataSetIndexUriParameterSetToStrict
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
name|sourceUri
argument_list|)
operator|.
name|to
argument_list|(
name|dataSetUriWithDataSetIndexSetToStrict
argument_list|)
operator|.
name|to
argument_list|(
name|resultUri
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|resultUri
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
operator|(
name|int
operator|)
name|size
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectsAscending
argument_list|(
name|header
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|)
operator|.
name|convertTo
argument_list|(
name|Number
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sourceUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalidDataSetIndexValueWithDataSetIndexUriParameterUnset ()
specifier|public
name|void
name|testInvalidDataSetIndexValueWithDataSetIndexUriParameterUnset
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
name|sourceUri
argument_list|)
operator|.
name|to
argument_list|(
name|dataSetUri
argument_list|)
operator|.
name|to
argument_list|(
name|resultUri
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|resultUri
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
operator|(
name|int
operator|)
name|size
argument_list|)
expr_stmt|;
name|result
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|==
operator|(
name|size
operator|/
literal|2
operator|)
condition|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sourceUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
operator|+
literal|10
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|sourceUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionError
name|assertionError
parameter_list|)
block|{
comment|// Check as much of the string as possible - but the ExchangeID at the end will be unique
name|String
name|expectedErrorString
init|=
name|dataSetUri
operator|+
literal|" Failed due to caught exception: "
operator|+
name|AssertionError
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|": Header: "
operator|+
name|Exchange
operator|.
name|DATASET_INDEX
operator|+
literal|" does not match. Expected: "
operator|+
name|size
operator|/
literal|2
operator|+
literal|" but was: "
operator|+
operator|(
name|size
operator|/
literal|2
operator|+
literal|10
operator|)
operator|+
literal|" on Exchange"
decl_stmt|;
name|String
name|actualErrorString
init|=
name|assertionError
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|actualErrorString
operator|.
name|startsWith
argument_list|(
name|expectedErrorString
argument_list|)
condition|)
block|{
comment|// This is what we expect
return|return;
block|}
else|else
block|{
throw|throw
name|assertionError
throw|;
block|}
block|}
name|fail
argument_list|(
literal|"AssertionError should have been generated"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalidDataSetIndexValueWithDataSetIndexUriParameterSetToOff ()
specifier|public
name|void
name|testInvalidDataSetIndexValueWithDataSetIndexUriParameterSetToOff
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
name|sourceUri
argument_list|)
operator|.
name|to
argument_list|(
name|dataSetUriWithDataSetIndexSetToOff
argument_list|)
operator|.
name|to
argument_list|(
name|resultUri
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|resultUri
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
operator|(
name|int
operator|)
name|size
argument_list|)
expr_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|==
operator|(
name|size
operator|/
literal|2
operator|)
condition|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sourceUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
operator|+
literal|10
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|sourceUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalidDataSetIndexValueWithDataSetIndexUriParameterSetToLenient ()
specifier|public
name|void
name|testInvalidDataSetIndexValueWithDataSetIndexUriParameterSetToLenient
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
name|sourceUri
argument_list|)
operator|.
name|to
argument_list|(
name|dataSetUriWithDataSetIndexSetToLenient
argument_list|)
operator|.
name|to
argument_list|(
name|resultUri
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|resultUri
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
operator|(
name|int
operator|)
name|size
argument_list|)
expr_stmt|;
name|result
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|==
operator|(
name|size
operator|/
literal|2
operator|)
condition|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sourceUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
operator|+
literal|10
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|sourceUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionError
name|assertionError
parameter_list|)
block|{
comment|// Check as much of the string as possible - but the ExchangeID at the end will be unique
name|String
name|expectedErrorString
init|=
name|dataSetUriWithDataSetIndexSetToLenient
operator|+
literal|" Failed due to caught exception: "
operator|+
name|AssertionError
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|": Header: "
operator|+
name|Exchange
operator|.
name|DATASET_INDEX
operator|+
literal|" does not match. Expected: "
operator|+
name|size
operator|/
literal|2
operator|+
literal|" but was: "
operator|+
operator|(
name|size
operator|/
literal|2
operator|+
literal|10
operator|)
operator|+
literal|" on Exchange"
decl_stmt|;
name|String
name|actualErrorString
init|=
name|assertionError
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|actualErrorString
operator|.
name|startsWith
argument_list|(
name|expectedErrorString
argument_list|)
condition|)
block|{
comment|// This is what we expect
return|return;
block|}
else|else
block|{
throw|throw
name|assertionError
throw|;
block|}
block|}
name|fail
argument_list|(
literal|"AssertionError should have been generated"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalidDataSetIndexValueWithDataSetIndexUriParameterSetToStrict ()
specifier|public
name|void
name|testInvalidDataSetIndexValueWithDataSetIndexUriParameterSetToStrict
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
name|sourceUri
argument_list|)
operator|.
name|to
argument_list|(
name|dataSetUriWithDataSetIndexSetToStrict
argument_list|)
operator|.
name|to
argument_list|(
name|resultUri
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|resultUri
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
operator|(
name|int
operator|)
name|size
argument_list|)
expr_stmt|;
name|result
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|==
operator|(
name|size
operator|/
literal|2
operator|)
condition|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sourceUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
operator|+
literal|10
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sourceUri
argument_list|,
name|dataSet
operator|.
name|getDefaultBody
argument_list|()
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionError
name|assertionError
parameter_list|)
block|{
comment|// Check as much of the string as possible - but the ExchangeID at the end will be unique
name|String
name|expectedErrorString
init|=
name|dataSetUriWithDataSetIndexSetToStrict
operator|+
literal|" Failed due to caught exception: "
operator|+
name|AssertionError
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|": Header: "
operator|+
name|Exchange
operator|.
name|DATASET_INDEX
operator|+
literal|" does not match. Expected: "
operator|+
name|size
operator|/
literal|2
operator|+
literal|" but was: "
operator|+
operator|(
name|size
operator|/
literal|2
operator|+
literal|10
operator|)
operator|+
literal|" on Exchange"
decl_stmt|;
name|String
name|actualErrorString
init|=
name|assertionError
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|actualErrorString
operator|.
name|startsWith
argument_list|(
name|expectedErrorString
argument_list|)
condition|)
block|{
comment|// This is what we expect
return|return;
block|}
else|else
block|{
throw|throw
name|assertionError
throw|;
block|}
block|}
name|fail
argument_list|(
literal|"AssertionError should have been generated"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

