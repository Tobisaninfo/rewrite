/*
 * Copyright 2011 <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ocpsoft.rewrite.servlet.config;

import java.util.List;

import com.ocpsoft.rewrite.EvaluationContext;
import com.ocpsoft.rewrite.config.ConditionBuilder;
import com.ocpsoft.rewrite.config.Operation;
import com.ocpsoft.rewrite.config.Rule;
import com.ocpsoft.rewrite.event.InboundRewrite;
import com.ocpsoft.rewrite.event.OutboundRewrite;
import com.ocpsoft.rewrite.event.Rewrite;
import com.ocpsoft.rewrite.servlet.config.parameters.ParameterBinding;
import com.ocpsoft.rewrite.servlet.config.parameters.Parameterized;
import com.ocpsoft.rewrite.servlet.http.event.HttpInboundServletRewrite;
import com.ocpsoft.rewrite.servlet.http.event.HttpOutboundServletRewrite;

/**
 * {@link Rule} that creates a bi-directional rewrite rule between an externally facing URL and an internal server
 * resource URL
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class Join implements Rule, Parameterized<LinkParameter>
{
   private final String pattern;
   private String resource;
   private final Path path;
   private Operation operation;

   protected Join(final String pattern)
   {
      this.pattern = pattern;
      this.path = Path.matches(pattern);
   }

   public static Join path(final String pattern)
   {
      return new Join(pattern);
   }

   public Join to(final String resource)
   {
      this.resource = resource;
      return this;
   }

   @Override
   public boolean evaluate(final Rewrite event, final EvaluationContext context)
   {
      if (event instanceof HttpInboundServletRewrite)
      {
         path.withRequestBinding();
         return path.evaluate(event, context);
      }

      else if (event instanceof HttpOutboundServletRewrite)
      {
         List<String> parameterNames = path.getPathExpression().getParameterNames();
         ConditionBuilder outbound = Path.matches(resource);
         for (String name : parameterNames) {
            outbound = outbound.and(QueryString.parameterExists(name));
         }
         return outbound.evaluate(event, context);
      }

      return false;
   }

   @Override
   public void perform(final Rewrite event, final EvaluationContext context)
   {
      if (event instanceof InboundRewrite)
      {
         Forward.to(resource).perform(event, context);
      }

      else if (event instanceof OutboundRewrite)
      {
         Substitute.with(pattern).perform(event, context);
      }

      if (operation != null)
         operation.perform(event, context);
   }

   @Override
   public LinkParameter where(final String parameter)
   {
      return new LinkParameter(this, path.getPathExpression().getParameter(parameter));
   }

   @Override
   public LinkParameter where(final String param, final String pattern)
   {
      return where(param).matches(pattern);
   }

   @Override
   public LinkParameter where(final String param, final String pattern, final ParameterBinding binding)
   {
      return where(param, pattern).bindsTo(binding);
   }

   @Override
   public LinkParameter where(final String param, final ParameterBinding binding)
   {
      return where(param).bindsTo(binding);
   }

   public Join and(final Operation operation)
   {
      this.operation = operation;
      return this;
   }
}