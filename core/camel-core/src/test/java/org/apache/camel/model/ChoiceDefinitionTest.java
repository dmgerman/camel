begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|TestSupport
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|ChoiceDefinitionTest
specifier|public
class|class
name|ChoiceDefinitionTest
extends|extends
name|TestSupport
block|{
annotation|@
name|Test
DECL|method|testChoiceOutputOrder ()
specifier|public
name|void
name|testChoiceOutputOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|ChoiceDefinition
name|choice
init|=
operator|new
name|ChoiceDefinition
argument_list|()
decl_stmt|;
name|WhenDefinition
name|when1
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
argument_list|)
decl_stmt|;
name|WhenDefinition
name|when2
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Donkey"
argument_list|)
argument_list|)
decl_stmt|;
name|OtherwiseDefinition
name|other
init|=
operator|new
name|OtherwiseDefinition
argument_list|()
decl_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when1
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when2
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when1
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|other
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"choice[when[{simple{${body}} contains Camel}],when[{simple{${body}} contains Donkey}],otherwise[]]"
argument_list|,
name|choice
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testChoiceOutputOrderIterate ()
specifier|public
name|void
name|testChoiceOutputOrderIterate
parameter_list|()
throws|throws
name|Exception
block|{
name|ChoiceDefinition
name|choice
init|=
operator|new
name|ChoiceDefinition
argument_list|()
decl_stmt|;
name|WhenDefinition
name|when1
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
argument_list|)
decl_stmt|;
name|WhenDefinition
name|when2
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Donkey"
argument_list|)
argument_list|)
decl_stmt|;
name|OtherwiseDefinition
name|other
init|=
operator|new
name|OtherwiseDefinition
argument_list|()
decl_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when1
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when2
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|def
range|:
name|choice
operator|.
name|getOutputs
argument_list|()
control|)
block|{
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
name|assertEquals
argument_list|(
name|when1
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|==
literal|1
condition|)
block|{
name|assertEquals
argument_list|(
name|when2
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
name|other
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
name|i
operator|++
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testChoiceOutputOrderNoOtherwise ()
specifier|public
name|void
name|testChoiceOutputOrderNoOtherwise
parameter_list|()
throws|throws
name|Exception
block|{
name|ChoiceDefinition
name|choice
init|=
operator|new
name|ChoiceDefinition
argument_list|()
decl_stmt|;
name|WhenDefinition
name|when1
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
argument_list|)
decl_stmt|;
name|WhenDefinition
name|when2
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Donkey"
argument_list|)
argument_list|)
decl_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when1
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when1
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testChoiceOutputOrderNoOtherwiseIterate ()
specifier|public
name|void
name|testChoiceOutputOrderNoOtherwiseIterate
parameter_list|()
throws|throws
name|Exception
block|{
name|ChoiceDefinition
name|choice
init|=
operator|new
name|ChoiceDefinition
argument_list|()
decl_stmt|;
name|WhenDefinition
name|when1
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
argument_list|)
decl_stmt|;
name|WhenDefinition
name|when2
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Donkey"
argument_list|)
argument_list|)
decl_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when1
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when1
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|def
range|:
name|choice
operator|.
name|getOutputs
argument_list|()
control|)
block|{
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
name|assertEquals
argument_list|(
name|when1
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|==
literal|1
condition|)
block|{
name|assertEquals
argument_list|(
name|when2
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
name|i
operator|++
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testChoiceOtherwiseAlwaysLast ()
specifier|public
name|void
name|testChoiceOtherwiseAlwaysLast
parameter_list|()
throws|throws
name|Exception
block|{
name|ChoiceDefinition
name|choice
init|=
operator|new
name|ChoiceDefinition
argument_list|()
decl_stmt|;
name|WhenDefinition
name|when1
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
argument_list|)
decl_stmt|;
name|WhenDefinition
name|when2
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Donkey"
argument_list|)
argument_list|)
decl_stmt|;
name|OtherwiseDefinition
name|other
init|=
operator|new
name|OtherwiseDefinition
argument_list|()
decl_stmt|;
comment|// add otherwise in between
name|choice
operator|.
name|addOutput
argument_list|(
name|when1
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when2
argument_list|)
expr_stmt|;
comment|// should ensure otherwise is last
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when1
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|other
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testChoiceOtherwiseAlwaysLastIterate ()
specifier|public
name|void
name|testChoiceOtherwiseAlwaysLastIterate
parameter_list|()
throws|throws
name|Exception
block|{
name|ChoiceDefinition
name|choice
init|=
operator|new
name|ChoiceDefinition
argument_list|()
decl_stmt|;
name|WhenDefinition
name|when1
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
argument_list|)
decl_stmt|;
name|WhenDefinition
name|when2
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Donkey"
argument_list|)
argument_list|)
decl_stmt|;
name|OtherwiseDefinition
name|other
init|=
operator|new
name|OtherwiseDefinition
argument_list|()
decl_stmt|;
comment|// add otherwise in between
name|choice
operator|.
name|addOutput
argument_list|(
name|when1
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when2
argument_list|)
expr_stmt|;
comment|// should ensure otherwise is last
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when1
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|other
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|def
range|:
name|choice
operator|.
name|getOutputs
argument_list|()
control|)
block|{
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
name|assertEquals
argument_list|(
name|when1
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|==
literal|1
condition|)
block|{
name|assertEquals
argument_list|(
name|when2
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
name|other
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
name|i
operator|++
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testChoiceOutputRemoveFirst ()
specifier|public
name|void
name|testChoiceOutputRemoveFirst
parameter_list|()
throws|throws
name|Exception
block|{
name|ChoiceDefinition
name|choice
init|=
operator|new
name|ChoiceDefinition
argument_list|()
decl_stmt|;
name|WhenDefinition
name|when1
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
argument_list|)
decl_stmt|;
name|WhenDefinition
name|when2
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Donkey"
argument_list|)
argument_list|)
decl_stmt|;
name|OtherwiseDefinition
name|other
init|=
operator|new
name|OtherwiseDefinition
argument_list|()
decl_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when1
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when2
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|other
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testChoiceOutputRemoveLast ()
specifier|public
name|void
name|testChoiceOutputRemoveLast
parameter_list|()
throws|throws
name|Exception
block|{
name|ChoiceDefinition
name|choice
init|=
operator|new
name|ChoiceDefinition
argument_list|()
decl_stmt|;
name|WhenDefinition
name|when1
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
argument_list|)
decl_stmt|;
name|WhenDefinition
name|when2
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Donkey"
argument_list|)
argument_list|)
decl_stmt|;
name|OtherwiseDefinition
name|other
init|=
operator|new
name|OtherwiseDefinition
argument_list|()
decl_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when1
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when2
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|remove
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when1
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testChoiceOutputSetFirst ()
specifier|public
name|void
name|testChoiceOutputSetFirst
parameter_list|()
throws|throws
name|Exception
block|{
name|ChoiceDefinition
name|choice
init|=
operator|new
name|ChoiceDefinition
argument_list|()
decl_stmt|;
name|WhenDefinition
name|when1
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
argument_list|)
decl_stmt|;
name|WhenDefinition
name|when2
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Donkey"
argument_list|)
argument_list|)
decl_stmt|;
name|WhenDefinition
name|when3
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Beer"
argument_list|)
argument_list|)
decl_stmt|;
name|OtherwiseDefinition
name|other
init|=
operator|new
name|OtherwiseDefinition
argument_list|()
decl_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when1
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when2
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|set
argument_list|(
literal|0
argument_list|,
name|when3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when3
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|when2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|other
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testChoiceOutputClear ()
specifier|public
name|void
name|testChoiceOutputClear
parameter_list|()
throws|throws
name|Exception
block|{
name|ChoiceDefinition
name|choice
init|=
operator|new
name|ChoiceDefinition
argument_list|()
decl_stmt|;
name|WhenDefinition
name|when1
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
argument_list|)
decl_stmt|;
name|WhenDefinition
name|when2
init|=
operator|new
name|WhenDefinition
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Donkey"
argument_list|)
argument_list|)
decl_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when1
argument_list|)
expr_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|when2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|choice
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

