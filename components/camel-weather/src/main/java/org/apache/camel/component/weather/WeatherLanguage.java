begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.weather
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|weather
package|;
end_package

begin_comment
comment|/**  * All available languages for the weather API.  */
end_comment

begin_enum
DECL|enum|WeatherLanguage
specifier|public
enum|enum
name|WeatherLanguage
block|{
DECL|enumConstant|en
name|en
block|,
DECL|enumConstant|ru
name|ru
block|,
DECL|enumConstant|it
name|it
block|,
DECL|enumConstant|es
name|es
block|,
DECL|enumConstant|sp
name|sp
block|,
DECL|enumConstant|uk
name|uk
block|,
DECL|enumConstant|ua
name|ua
block|,
DECL|enumConstant|de
name|de
block|,
DECL|enumConstant|pt
name|pt
block|,
DECL|enumConstant|ro
name|ro
block|,
DECL|enumConstant|pl
name|pl
block|,
DECL|enumConstant|fi
name|fi
block|,
DECL|enumConstant|nl
name|nl
block|,
DECL|enumConstant|fr
name|fr
block|,
DECL|enumConstant|bg
name|bg
block|,
DECL|enumConstant|sv
name|sv
block|,
DECL|enumConstant|se
name|se
block|,
DECL|enumConstant|zh_tw
name|zh_tw
block|,
DECL|enumConstant|zh
name|zh
block|,
DECL|enumConstant|zh_cn
name|zh_cn
block|,
DECL|enumConstant|tr
name|tr
block|,
DECL|enumConstant|hr
name|hr
block|,
DECL|enumConstant|ca
name|ca
block|}
end_enum

end_unit

