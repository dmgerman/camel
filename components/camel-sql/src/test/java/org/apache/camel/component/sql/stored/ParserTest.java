begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
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
name|component
operator|.
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|TemplateStoredProcedureFactory
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
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|ast
operator|.
name|InputParameter
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
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|ast
operator|.
name|OutParameter
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
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|ast
operator|.
name|ParseRuntimeException
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
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|ast
operator|.
name|Template
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
name|junit
operator|.
name|Assert
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
DECL|class|ParserTest
specifier|public
class|class
name|ParserTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|parser
name|TemplateStoredProcedureFactory
name|parser
init|=
operator|new
name|TemplateStoredProcedureFactory
argument_list|(
literal|null
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldParseOk ()
specifier|public
name|void
name|shouldParseOk
parameter_list|()
block|{
name|Template
name|template
init|=
name|parser
operator|.
name|parseTemplate
argument_list|(
literal|"addnumbers(INTEGER ${header.header1},"
operator|+
literal|"VARCHAR ${property.property1},BIGINT ${header.header2},OUT INTEGER header1)"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"addnumbers"
argument_list|,
name|template
operator|.
name|getProcedureName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|template
operator|.
name|getInputParameterList
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"header1"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"property1"
argument_list|,
literal|"constant string"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"header2"
argument_list|,
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|InputParameter
name|param1
init|=
name|template
operator|.
name|getInputParameterList
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"_0"
argument_list|,
name|param1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|param1
operator|.
name|getSqlType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|,
name|param1
operator|.
name|getValueExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|InputParameter
name|param2
init|=
name|template
operator|.
name|getInputParameterList
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"_1"
argument_list|,
name|param2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|param2
operator|.
name|getSqlType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"constant string"
argument_list|,
name|param2
operator|.
name|getValueExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|InputParameter
name|param3
init|=
name|template
operator|.
name|getInputParameterList
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"_2"
argument_list|,
name|param3
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Types
operator|.
name|BIGINT
argument_list|,
name|param3
operator|.
name|getSqlType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|,
name|param3
operator|.
name|getValueExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|BigInteger
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|OutParameter
name|sptpOutputNode
init|=
name|template
operator|.
name|getOutParameterList
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"_3"
argument_list|,
name|sptpOutputNode
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|sptpOutputNode
operator|.
name|getSqlType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"header1"
argument_list|,
name|sptpOutputNode
operator|.
name|getOutHeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ParseRuntimeException
operator|.
name|class
argument_list|)
DECL|method|noOutputParameterShouldFail ()
specifier|public
name|void
name|noOutputParameterShouldFail
parameter_list|()
block|{
name|parser
operator|.
name|parseTemplate
argument_list|(
literal|"ADDNUMBERS2"
operator|+
literal|"(INTEGER VALUE1 ${header.v1},INTEGER VALUE2 ${header.v2})"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ParseRuntimeException
operator|.
name|class
argument_list|)
DECL|method|unexistingTypeShouldFail ()
specifier|public
name|void
name|unexistingTypeShouldFail
parameter_list|()
block|{
name|parser
operator|.
name|parseTemplate
argument_list|(
literal|"ADDNUMBERS2"
operator|+
literal|"(XML VALUE1 ${header.v1},OUT INTEGER VALUE2 ${header.v2})"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ParseRuntimeException
operator|.
name|class
argument_list|)
DECL|method|unmappedTypeShouldFaild ()
specifier|public
name|void
name|unmappedTypeShouldFaild
parameter_list|()
block|{
name|parser
operator|.
name|parseTemplate
argument_list|(
literal|"ADDNUMBERS2"
operator|+
literal|"(OTHER VALUE1 ${header.v1},INTEGER VALUE2 ${header.v2})"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

